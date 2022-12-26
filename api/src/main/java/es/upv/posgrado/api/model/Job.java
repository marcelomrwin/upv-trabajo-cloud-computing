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
@NamedQueries({
        @NamedQuery(name = "Job.findById",query = "from Job where id = ?1"),
        @NamedQuery(name = "Job.findAllJobs",query = "from Job order by requestedAt,processedAt desc"),
        @NamedQuery(name = "Job.findByIdSubmittedBy",query = "from Job where id = ?1 and lower(submittedBy) like concat('%',lower(?2),'%')"),
        @NamedQuery(name = "Job.findAllSubmittedBy",query = "from Job where lower(submittedBy) like concat('%',lower(?1),'%') order by requestedAt desc"),
        @NamedQuery(name = "Job.findByTitleSubmittedBy",query = "from Job where lower(submittedBy) like concat('%',lower(?1),'%') and lower(title) like concat('%',lower(?2),'%') order by requestedAt,processedAt desc"),
        @NamedQuery(name = "Job.findByIdAndTitleSubmittedBy",query = "from Job where id = ?1 and lower(submittedBy) like concat('%',lower(?2),'%') and lower(title) like concat('%',lower(?3),'%') order by requestedAt,processedAt desc")
})
public class Job extends PanacheEntityBase {
    @Id
    private Long id;
    private String title;
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(columnDefinition = "text")
    private String result;

    private LocalDateTime requestedAt;

    private LocalDateTime processedAt;

    private String processedBy;

    @Column(columnDefinition = "text")
    private String thumbnail;

    private String submittedBy;

    private String elapsedTime;
}
