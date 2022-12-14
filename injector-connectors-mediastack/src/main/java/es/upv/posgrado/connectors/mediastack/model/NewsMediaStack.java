package es.upv.posgrado.connectors.mediastack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsMediaStack {
    public String author;
    public String title;
    public String description;
    public String url;
    public String source;
    public String image;
    public String category;
    public String language;
    public String country;
    public String published_at;
}
