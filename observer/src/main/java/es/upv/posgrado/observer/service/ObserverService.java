package es.upv.posgrado.observer.service;

import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.common.model.monitoring.Event;
import es.upv.posgrado.common.model.monitoring.JobStatistics;
import es.upv.posgrado.common.model.monitoring.MonitoringResponse;
import es.upv.posgrado.observer.client.cache.CacheClient;
import es.upv.posgrado.observer.client.messaging.KafkaProducer;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

@ApplicationScoped
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObserverService {

    @Inject
    ManagedExecutor managedExecutor;
    @Inject
    CacheClient cacheClient;
    private AtomicLong totalJobRequest = new AtomicLong();
    private AtomicLong totalJobResponse = new AtomicLong();
    private AtomicLong totalTimeExpend = new AtomicLong();
    private LocalDateTime startDate;
    private LocalDateTime olderJob;

    private long timeFrameDuration = 5l;
    private ChronoUnit timeFrameUnit = ChronoUnit.MINUTES;

    @Inject
    private KafkaProducer kafkaProducer;

    public void updateTimeFrameDuration(@NotNull @Min(1) Long newDuration) {
        this.timeFrameDuration = newDuration;
    }

    public void updateTimeFrameUnit(ChronoUnit chronoUnit) {
        this.timeFrameUnit = chronoUnit;
    }

    private synchronized void verifyOldProducer(LocalDateTime newDate) {
        if (olderJob == null) olderJob = newDate;
        else {
            if (newDate.isBefore(olderJob)) olderJob = newDate;
        }
    }

    void startup(@Observes StartupEvent event) {
        startDate = LocalDateTime.now();
        log.debug("Registered Observer App " + startDate);
    }

    @Scheduled(every = "{app.metric.window}", concurrentExecution = SKIP)
    public void scheduleReport() {
        log.debug("Generating Statistics Report Starts");
        try {
            generateReport().get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("Generating Statistics Report Ends");
    }

    private CompletableFuture<Void> generateReport() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                JobStatistics jobStatistics = JobStatistics.fromMetrics(totalTimeExpend.get(), totalJobResponse.get(), totalJobRequest.get(), olderJob);

                MonitoringResponse monitoring = retrieveMonitoring();
                monitoring.updateCurrentStatistics(jobStatistics);
                generateTimeFrameStatistics(monitoring);
                storeMonitoring(monitoring);
                kafkaProducer.sendMessage(monitoring);
            } catch (Exception e) {
                log.error("Fail reporting JobStatistics", e);
            }

        }, managedExecutor);
        return future;
    }

    private void generateTimeFrameStatistics(MonitoringResponse monitoring) {
        log.debug("Generating Timeframe for events\n{}", monitoring.getEventHistory() != null ? monitoring.getEventHistory().parallelStream().collect(Collectors.groupingBy(event -> event.getStatus(), Collectors.counting())) : 0);
        JobStatistics timeFrameStatistics = JobStatistics.fromEventByTimeFrame(monitoring.getEventHistory(), timeFrameDuration, timeFrameUnit);
        monitoring.setTimeFrameStatistics(timeFrameStatistics);
    }

    public CompletableFuture<Void> registerJobOperation(Job job) {
        final MonitoringResponse monitor = retrieveMonitoring();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            populateMonitor(job, monitor);
            storeMonitoring(monitor);
        }, managedExecutor);
        return future;
    }

    public void populateMonitor(Job job, MonitoringResponse monitor) {
        Event event = Event.builder()
                .status(job.getStatus())
                .date(LocalDateTime.now())
                .build();
        switch (job.getStatus()) {
            case SUBMITTED: {
                long actualTotalJobRequest = totalJobRequest.get();
                totalJobRequest.incrementAndGet();
                log.debug("Job Request changing from {} to {}", actualTotalJobRequest, totalJobRequest.get());
                verifyOldProducer(job.getRequestedAt());
                event.setDate(job.getRequestedAt());
                monitor.addEvent(event);
                break;
            }
            case PROCESSING:
                monitor.addEvent(event);
                break;//no metrics for processing
            case FINISHED: {
                totalJobResponse.incrementAndGet();
                Duration duration = Duration.between(job.getRequestedAt(), job.getProcessedAt());
                totalTimeExpend.addAndGet(duration.toMillis());
                event.setDuration(duration.toMillis());
                event.setDate(job.getProcessedAt());
                monitor.addEvent(event);
                break;
            }
        }
    }

    public synchronized MonitoringResponse retrieveMonitoring() {
        MonitoringResponse monitoringResponse = cacheClient.get(MonitoringResponse.class.getName());
        if (monitoringResponse != null) {
            return monitoringResponse;
        }
        return MonitoringResponse.builder().build();
    }

    public synchronized void storeMonitoring(MonitoringResponse monitoringResponse) {
        cacheClient.put(MonitoringResponse.class.getName(), monitoringResponse);
    }
}
