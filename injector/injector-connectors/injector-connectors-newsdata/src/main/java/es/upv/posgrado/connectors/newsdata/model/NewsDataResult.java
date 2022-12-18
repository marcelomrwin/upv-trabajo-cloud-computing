package es.upv.posgrado.connectors.newsdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDataResult {
    public String title;
    public String link;
    public Object keywords;
    public Object creator;
    public Object video_url;
    public String description;
    public Object content;
    public String pubDate;
    public String image_url;
    public String source_id;
    public List<String> country;
    public List<String> category;
    public String language;
}
