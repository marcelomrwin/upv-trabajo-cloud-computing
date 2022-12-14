package es.upv.posgrado.injector.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class News extends PanacheEntity {
    @Column(length = 1000)
    @Size(max = 1000)
    private String title;
    @Column(length = 2000)
    @Size(max = 2000)
    private String description;
    @Column(length = 600)
    @Size(max = 600)
    private String urlToImage;
    private LocalDateTime publishedAt;

    private LocalDateTime generatedAt;
}
