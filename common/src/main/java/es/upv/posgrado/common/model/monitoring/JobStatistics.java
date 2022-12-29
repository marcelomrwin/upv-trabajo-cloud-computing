package es.upv.posgrado.common.model.monitoring;

import es.upv.posgrado.common.model.JobStatus;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobStatistics implements Comparable<JobStatistics> {
    private LocalDateTime reportDate;
    private Long totalJobRequest;
    private Long totalJobResponse;
    private Long averageRateArrival;
    private Long averageResponseTime;
    private String averageResponseTimeFormatted;

    public static JobStatistics fromEventByTimeFrame(SortedSet<Event> events, long amount, ChronoUnit chronoUnit) {

        LocalDateTime startTimeFrame = LocalDateTime.now().minus(amount, chronoUnit);
        JobStatistics jobStatistics = JobStatistics.builder().averageRateArrival(0L).averageResponseTime(0L).averageResponseTime(0L).averageResponseTimeFormatted("00:00:00.000").build();
        if (events != null && events.size() > 0) {
            Map<JobStatus, Long> eventsTimeFrame = events.parallelStream().filter(event -> event.getDate().isAfter(startTimeFrame)).collect(Collectors.groupingBy(event -> event.getStatus(), Collectors.counting()));

            //requested
            long totalJobRequest = eventsTimeFrame.getOrDefault(JobStatus.SUBMITTED,0L);
            //finished
            long totalJobResponse = eventsTimeFrame.getOrDefault(JobStatus.FINISHED,0L);
            //processing
            long totalProcessing = eventsTimeFrame.getOrDefault(JobStatus.PROCESSING,0L);

            long totalTimeExpend = events.stream().filter(event -> event.getStatus() == JobStatus.FINISHED && event.getDate().isAfter(startTimeFrame)).mapToLong(event -> event.getDuration()).sum();
            Optional<LocalDateTime> startDate = events.stream().filter(event -> event.getDate().isAfter(startTimeFrame)).min(Comparator.comparing(Event::getDate)).map(event -> event.getDate());
            jobStatistics = fromMetrics(totalTimeExpend, totalJobResponse, totalJobRequest, startDate.orElse(LocalDateTime.now()));
        }

        return jobStatistics;
    }

    public static JobStatistics fromMetrics(long totalTimeExpend, long totalJobResponse, long totalJobRequest, LocalDateTime startDate) {
        Long averageResponseTime = 0L;
        if (totalTimeExpend > 0 & totalJobResponse > 0L)
            averageResponseTime = totalTimeExpend / totalJobResponse; //mills
        Duration duration = Duration.ofMillis(averageResponseTime);
        String averageResponseTimeFormatted = String.format("%02d:%02d:%02d.%03d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart(), duration.toMillisPart());

        LocalDateTime start = startDate;
        if (start == null) start = LocalDateTime.now();

        Duration durationFromStart = Duration.between(start, LocalDateTime.now());
        long minutes = durationFromStart.toMinutes();

        long jobsPerMinute = 0L;
        if (totalJobRequest > 0L && minutes > 0L)
            jobsPerMinute = totalJobRequest / minutes;

        JobStatistics jobStatistics = JobStatistics.builder()
                .reportDate(LocalDateTime.now())
                .totalJobRequest(totalJobRequest)
                .totalJobResponse(totalJobResponse)
                .averageResponseTime(averageResponseTime)
                .averageResponseTimeFormatted(averageResponseTimeFormatted)
                .averageRateArrival(jobsPerMinute)
                .build();
        return jobStatistics;
    }

    @Override
    public int compareTo(JobStatistics o) {
        return getReportDate().compareTo(o.getReportDate());
    }
}
