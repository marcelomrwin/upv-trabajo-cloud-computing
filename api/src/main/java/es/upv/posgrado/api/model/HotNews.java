package es.upv.posgrado.api.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotNews extends PanacheEntityBase {
    @Id
    private Long id;
    private String title;
    private LocalDateTime publishedAt;
    @Column(columnDefinition = "text")
    private String thumbnail;

}
