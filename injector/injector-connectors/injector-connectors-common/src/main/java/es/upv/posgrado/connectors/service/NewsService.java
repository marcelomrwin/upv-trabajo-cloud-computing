package es.upv.posgrado.connectors.service;



import es.upv.posgrado.common.model.NewsDTO;

import java.util.Set;

public interface NewsService {
    Set<NewsDTO> getNews();
}
