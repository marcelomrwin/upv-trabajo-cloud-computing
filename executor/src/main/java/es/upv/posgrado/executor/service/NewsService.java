package es.upv.posgrado.executor.service;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.executor.repository.NewsRepository;
import es.upv.posgrado.executor.repository.NewsSource;
import es.upv.posgrado.executor.service.exception.NewsNotFoundException;
import io.quarkus.arc.All;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class NewsService {

    @Inject
    @All
    List<NewsSource> newsSources;

    @Inject
    NewsRepository newsRepository;

    public NewsDTO findNews(Long id) throws NewsNotFoundException {

        for (NewsSource s : newsSources) {
            NewsDTO dto = s.findHotNews(id);
            if (dto != null) return dto;
        }

        throw new NewsNotFoundException("News " + id + " not found");
    }

    @Transactional
    public void saveNews(NewsDTO newsDTO) {
        newsRepository.saveNews(newsDTO);
    }

}
