package es.upv.posgrado.executor.repository;

import es.upv.posgrado.common.model.NewsDTO;

public interface NewsSource {

    NewsDTO findHotNews(Long id);
}
