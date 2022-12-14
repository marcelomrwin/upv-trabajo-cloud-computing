package es.upv.posgrado.connectors.newsapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;
}
