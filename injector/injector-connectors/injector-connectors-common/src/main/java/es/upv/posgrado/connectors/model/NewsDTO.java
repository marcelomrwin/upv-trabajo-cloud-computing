package es.upv.posgrado.connectors.model;

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
    private String title;
    private String description;
    private String urlToImage;
    private String thumbnail;
    private LocalDateTime publishedAt;
}
