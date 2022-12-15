package es.upv.posgrado.executor.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocalJobDTO {
    private Long id;
    private String title;
    private String description;
    private String publishedAt;
    private String generatedAt;
    private String image;
}
