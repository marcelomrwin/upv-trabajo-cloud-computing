package es.upv.posgrado.executor.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String publishedAt;
    private String generatedAt;
    private String image;
}
