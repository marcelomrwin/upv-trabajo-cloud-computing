package es.upv.posgrado.common.model.monitoring;

import lombok.*;

import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

@Data
@Builder
public class MonitoringResponse {

    @Setter(AccessLevel.PRIVATE)
    private JobStatistics currentStatistics;
    private SortedSet<JobStatistics> statisticsHistory;
    private SortedSet<Event> eventHistory;

    private JobStatistics timeFrameStatistics;


    public void updateCurrentStatistics(@NonNull JobStatistics jobStatistics) {
        if (this.currentStatistics != null) addJobStatistics(this.currentStatistics);
        this.currentStatistics = jobStatistics;
    }

    public void addEvent(Event event) {
        if (this.eventHistory == null) this.eventHistory = new ConcurrentSkipListSet<>();
        this.eventHistory.add(event);
    }

    public void addJobStatistics(JobStatistics jobStatistics) {
        if (this.statisticsHistory == null) this.statisticsHistory = new ConcurrentSkipListSet<>();
        this.statisticsHistory.add(jobStatistics);
    }
}
