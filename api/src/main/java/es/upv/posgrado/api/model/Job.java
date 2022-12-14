package es.upv.posgrado.api.model;

import es.upv.posgrado.common.model.JobStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job extends PanacheEntityBase {
    @Id
    private Long id;
    private String title;
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Lob
    private String result;

    private LocalDateTime requestedAt;

    private LocalDateTime processedAt;

    private String processedBy;
}
