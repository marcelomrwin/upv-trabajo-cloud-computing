package es.upv.posgrado.api.model;

import es.upv.posgrado.common.model.NewsStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedQueries({
        @NamedQuery(name = "HotNews.findByTitle",query = "from HotNews where lower(title) like concat('%',lower(?1),'%') order by id desc"),
        @NamedQuery(name = "HotNews.findByTitleNotSubmitted",query = "from HotNews where lower(title) like concat('%',lower(?1),'%') and status != 'SUBMITTED' order by id desc"),
        @NamedQuery(name = "HotNews.findAllNotSubmitted",query = "from HotNews where status != 'SUBMITTED' order by id desc")
})
public class HotNews extends PanacheEntityBase {
    @Id
    private Long id;
    private String title;
    private LocalDateTime publishedAt;
    @Column(columnDefinition = "text")
    private String thumbnail;
    @Enumerated(EnumType.STRING)
    private NewsStatus status;

}
