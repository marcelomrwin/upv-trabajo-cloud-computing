package es.upv.posgrado.common.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JobDTO {
    private Long id;
    private String title;
    private LocalDateTime publishedAt;
    private JobStatus status;
    private String result;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private String processedBy;
}
