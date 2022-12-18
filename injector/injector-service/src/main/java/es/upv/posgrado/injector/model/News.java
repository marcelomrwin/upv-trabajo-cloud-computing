package es.upv.posgrado.injector.model;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class News extends PanacheEntity {
	@Column(length = 1000, unique = true, updatable = false)
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
	@Column(columnDefinition = "text")
	private String thumbnail;

	public static Optional<PanacheEntityBase> findByTitle(String title) {
		return find("title", title).firstResultOptional();
	}

}
