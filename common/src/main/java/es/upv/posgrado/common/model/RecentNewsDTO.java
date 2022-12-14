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
public class RecentNewsDTO {
    protected Long id;
    protected String title;
    protected LocalDateTime publishedAt;
}
