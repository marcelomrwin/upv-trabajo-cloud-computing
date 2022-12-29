package es.upv.posgrado.observer;

import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.common.model.JobStatus;
import es.upv.posgrado.common.model.monitoring.JobStatistics;
import es.upv.posgrado.common.model.monitoring.MonitoringResponse;
import es.upv.posgrado.observer.client.cache.CacheClient;
import es.upv.posgrado.observer.client.messaging.KafkaProducer;
import es.upv.posgrado.observer.service.ObserverService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;


public class WindowFrameTest {

    static MonitoringResponse monitoringResponse = MonitoringResponse.builder().build();

    static ObserverService observerService = new ObserverService();

    @BeforeAll
    public static void generateStatistics() throws ExecutionException, InterruptedException, TimeoutException {

        KafkaProducer mockKafkaProducer = Mockito.mock(KafkaProducer.class);
        Mockito.doNothing().when(mockKafkaProducer).sendMessage(Mockito.any(MonitoringResponse.class));
//        QuarkusMock.installMockForType(mockKafkaProducer, KafkaProducer.class);

//        ObserverService mockObserverService = Mockito.mock(ObserverService.class);
//        Mockito.when(mockObserverService.retrieveMonitoring()).thenReturn(monitoringResponse);
//        Mockito.doNothing().when(mockObserverService).storeMonitoring(Mockito.any(MonitoringResponse.class));
//        Mockito.doCallRealMethod().when(mockObserverService).populateMonitor(Mockito.any(Job.class), Mockito.any(MonitoringResponse.class));
//        QuarkusMock.installMockForType(mockObserverService, ObserverService.class);

        CacheClient mockCacheClient = Mockito.mock(CacheClient.class);
        Mockito.when(mockCacheClient.get(Mockito.anyString())).thenReturn(monitoringResponse);
        Mockito.doNothing().when(mockCacheClient).put(Mockito.anyString(), Mockito.any(MonitoringResponse.class));
//        QuarkusMock.installMockForType(mockCacheClient, CacheClient.class);

        observerService = ObserverService.builder()
                .cacheClient(mockCacheClient)
                .kafkaProducer(mockKafkaProducer)
                .managedExecutor(ManagedExecutor.builder().build())
                .totalJobResponse(new AtomicLong())
                .totalJobRequest(new AtomicLong())
                .totalTimeExpend(new AtomicLong())
                .build();

        Random random = new Random();

        for (int i = 0; i < 10000; i++) {

            JobStatus status = null;
            LocalDateTime date;
            long duration = 0L;

            switch (random.nextInt(0, 3)) {
                case 0:
                    status = JobStatus.SUBMITTED;
                    break;
                case 1:
                    status = JobStatus.PROCESSING;
                    break;
                case 2:
                    status = JobStatus.FINISHED;
                    break;
                default:
                    throw new IllegalArgumentException("Status Index not found");
            }
            long minusNow = random.nextLong(0, 5);
            date = LocalDateTime.now().minus(minusNow, ChronoUnit.MINUTES);
            if (random.nextBoolean()) {
                long amountToSubtract = random.nextLong(10, 20);
                date = date.minus(amountToSubtract, ChronoUnit.MINUTES);
            }
            duration = random.nextLong(100, 10001);

            Job job = Job.builder()
                    .status(status)
                    .requestedAt(date)
                    .processedAt(date.plus(duration, ChronoUnit.MILLIS))
                    .build();

            observerService.registerJobOperation(job).get(1, TimeUnit.MINUTES);

            if (i % 10 == 0 && random.nextBoolean()) {
                observerService.scheduleReport();
            }

        }
        observerService.scheduleReport();

    }

    @Test
    public void smoke() {
        monitoringResponse.getEventHistory().stream().forEach(event -> {
            System.out.println(event);
        });
        monitoringResponse.getStatisticsHistory().stream().forEach(jobStatistics -> {
            System.out.println(jobStatistics);
        });
        System.out.println(monitoringResponse.getCurrentStatistics());
        JobStatistics byTimeFrame = JobStatistics.fromEventByTimeFrame(monitoringResponse.getEventHistory(), 5L, ChronoUnit.MINUTES);
        System.out.println("\n"+ byTimeFrame+"\n");
    }


}
