package es.upv.posgrado.executor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.common.model.JobStatus;
import es.upv.posgrado.executor.client.injector.InjectorRestClient;
import es.upv.posgrado.executor.client.injector.model.NewsDTO;
import es.upv.posgrado.executor.client.messaging.KafkaProducer;
import es.upv.posgrado.executor.model.LocalJobDTO;
import es.upv.posgrado.executor.repository.ExecutorJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
@Slf4j
public class ExecutorService {

    @Inject
    ScriptExecutor scriptExecutor;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @RestClient
    InjectorRestClient injectorRestClient;

    @Inject
    ExecutorJobRepository executorJobRepository;

    @Inject
    ImageProcessorService imageProcessorService;

    @Inject
    KafkaProducer kafkaProducer;

    @Transactional
    public void consumeJobEvent(Job job) {
        try {log.warn("JOB HAS ID? {}",job.getId());
            executorJobRepository.saveJob(job);
            Long id = Long.valueOf(job.getId());
            NewsDTO newsDTO = injectorRestClient.getNewsById(id);
            String result = generateData(id, newsDTO.getTitle(), newsDTO.getDescription(), newsDTO.getGeneratedAt(), newsDTO.getPublishedAt(), newsDTO.getUrlToImage());
            job.setResult(result);
            job.setProcessedBy(getHostname());
            job.setProcessedAt(LocalDateTime.now());
            job.setStatus(JobStatus.FINISHED);
            executorJobRepository.updateJob(job);
            kafkaProducer.sendMessage(job);

        } catch (Exception e) {
            log.error("Error processing Job Request", e);
            job.setResult("Error processing Job Request\n"+e.getMessage());
            job.setStatus(JobStatus.FINISHED);
            job.setProcessedBy(getHostname());
            job.setProcessedAt(LocalDateTime.now());
            executorJobRepository.updateJob(job);
            kafkaProducer.sendMessage(job);
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
