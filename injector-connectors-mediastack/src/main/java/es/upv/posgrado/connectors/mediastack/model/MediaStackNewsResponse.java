package es.upv.posgrado.connectors.mediastack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaStackNewsResponse {
    public Pagination pagination;
    public List<NewsMediaStack> data;
}
