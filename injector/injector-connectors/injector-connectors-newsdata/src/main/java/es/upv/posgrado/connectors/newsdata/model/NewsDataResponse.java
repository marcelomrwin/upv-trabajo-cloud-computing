package es.upv.posgrado.connectors.newsdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDataResponse {
    public String status;
    public int totalResults;
    public List<NewsDataResult> results;
    public int nextPage;
}
