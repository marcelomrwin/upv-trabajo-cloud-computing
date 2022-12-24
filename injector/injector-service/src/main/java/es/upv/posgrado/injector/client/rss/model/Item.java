package es.upv.posgrado.injector.client.rss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String guid;
    private String author;
}
