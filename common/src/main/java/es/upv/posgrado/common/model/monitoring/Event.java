package es.upv.posgrado.common.model.monitoring;

import es.upv.posgrado.common.model.JobStatus;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class Event implements Comparable<Event>{
    private JobStatus status;
    private LocalDateTime date;

    private Long duration;

    @Override
    public int compareTo(Event o) {
        return date.compareTo(o.getDate());
    }
}
