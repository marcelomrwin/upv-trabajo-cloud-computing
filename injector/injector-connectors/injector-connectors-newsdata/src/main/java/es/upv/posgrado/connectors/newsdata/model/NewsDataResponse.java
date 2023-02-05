package es.upv.posgrado.connectors.newsdata.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String nextPage;
}
