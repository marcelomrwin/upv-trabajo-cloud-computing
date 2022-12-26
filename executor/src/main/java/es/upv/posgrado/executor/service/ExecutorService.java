package es.upv.posgrado.executor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.common.model.JobStatus;
import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.executor.client.messaging.KafkaProducer;
import es.upv.posgrado.executor.model.LocalJobDTO;
import es.upv.posgrado.executor.repository.ExecutorJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@Slf4j
public class ExecutorService {

    @ConfigProperty(name = "app.simulate.delay", defaultValue = "false")
    Boolean simulateDelay;

    @Inject
    ScriptExecutor scriptExecutor;

    @Inject
    ManagedExecutor executor;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ExecutorJobRepository executorJobRepository;

    @Inject
    ImageProcessorService imageProcessorService;

    @Inject
    KafkaProducer kafkaProducer;

    @Inject
    NewsService newsService;

    @Transactional
    public CompletableFuture<Void> consumeJobEvent(Job job) {
       CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            try {
                executorJobRepository.saveJob(job);
                kafkaProducer.sendMessage(job);

                delay();

                Long id = Long.valueOf(job.getId());
                NewsDTO newsDTO = newsService.findNews(id);

                String result = generateData(id, newsDTO.getTitle(), newsDTO.getDescription(), newsDTO.getGeneratedAt(), newsDTO.getPublishedAt(), newsDTO.getUrlToImage());
                job.setResult(result);
                job.setProcessedBy(getHostname());
                job.setProcessedAt(LocalDateTime.now());
                job.setStatus(JobStatus.FINISHED);

                calculateElapsedTime(job);

                executorJobRepository.updateJob(job);
                kafkaProducer.sendMessage(job);

            } catch (Exception e) {
                log.error("Error processing Job Request", e);
                job.setResult("Error processing Job Request\n" + e.getMessage());
                job.setStatus(JobStatus.FINISHED);
                job.setProcessedBy(getHostname());
                job.setProcessedAt(LocalDateTime.now());
                executorJobRepository.updateJob(job);
                kafkaProducer.sendMessage(job);
            }
        },executor);
       return future;
    }

    private void calculateElapsedTime(Job job) {
        Duration duration = Duration.between(job.getRequestedAt(),job.getProcessedAt());
        String text = String.format("%02d:%02d:%02d.%03d",duration.toHoursPart(),duration.toMinutesPart(),duration.toSecondsPart(),duration.toMillisPart());
        job.setElapsedTime(text);
    }

    private void delay() {
        if (simulateDelay) {
            Random random = new Random();
            long seconds = random.nextLong(10);
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                log.error("Fail while delaying method", e.getMessage());
            }
        }
    }

    @ActivateRequestContext
    public String generateData(Long id, String title, String description, LocalDateTime generatedAt, LocalDateTime publishedAt, String imageURL) throws Exception {
        LocalJobDTO localJobDTO = LocalJobDTO.builder().id(id).title(title).description(description)
                .generatedAt(generatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .publishedAt(publishedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build();
        localJobDTO.setImage(imageProcessorService.convertImageToBase64String(imageURL));
        File f = File.createTempFile("file", ".json");
        Files.writeString(f.toPath(), objectMapper.writeValueAsString(localJobDTO));
        log.info("calling python script");
        String html = scriptExecutor.executeScriptCommand(f.getAbsolutePath());
        f.delete();
        return html;
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

}
