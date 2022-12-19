package es.upv.posgrado.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Long id;
    private String title;
    private String description;
    private String urlToImage;
    private LocalDateTime publishedAt;
    private LocalDateTime generatedAt;
    private String thumbnail;
}
