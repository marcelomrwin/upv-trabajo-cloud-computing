package es.upv.posgrado.executor.client.injector.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO  {

    private String title;
    private String description;
    private String urlToImage;
    private LocalDateTime publishedAt;
    private LocalDateTime generatedAt;
}
