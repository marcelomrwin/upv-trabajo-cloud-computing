package es.upv.posgrado.executor.client.injector.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
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
