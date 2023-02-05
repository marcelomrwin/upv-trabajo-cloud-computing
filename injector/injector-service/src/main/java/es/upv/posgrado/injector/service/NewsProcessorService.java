package es.upv.posgrado.injector.service;


import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.injector.client.cache.CacheClient;
import es.upv.posgrado.injector.client.messaging.KafkaClient;
import es.upv.posgrado.injector.model.News;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class NewsProcessorService {

    @Inject
    KafkaClient kafkaClient;

    @Inject
    TransactionManager transactionManager;

    @Inject
    ImageService imageService;

    @Inject
    CacheClient cacheClient;

    public void saveNewsFromArticle(Set<NewsDTO> newsDTOSet) {
        if (!newsDTOSet.isEmpty()) {
            for (NewsDTO newsDTO : newsDTOSet) {

                if (News.findByTitle(newsDTO.getTitle()).isPresent())
                    continue;

                News newsEntity = News.builder().title(newsDTO.getTitle()).description(newsDTO.getDescription())
                        .publishedAt(newsDTO.getPublishedAt()).generatedAt(LocalDateTime.now()).build();
                processNewEntity(newsDTO, newsEntity);
            }
        }
    }

    private void processNewEntity(NewsDTO newsDTO, News newsEntity) {
        String urlToImage = imageService.downloadArticleImage(newsDTO);

        if (urlToImage == null) {
            newsDTO.setUrlToImage("https://m.media-amazon.com/images/I/51lEaBcxm3L.png");
            urlToImage = imageService.downloadArticleImage(newsDTO);
        }

        if (urlToImage != null) {
            newsEntity.setUrlToImage(urlToImage);
            newsEntity.setThumbnail(newsDTO.getThumbnail());

            try {
                log.info("Proceeding to save News {}", newsEntity.getTitle());
                transactionManager.begin();
                newsEntity.persistAndFlush();
                transactionManager.commit();
                newsDTO.setId(newsEntity.id);
                cacheClient.set(String.valueOf(newsDTO.getId()), newsDTO);
                kafkaClient.notifyRecentNews(newsEntity).get(5, TimeUnit.SECONDS);
            } catch (PersistenceException e) {
                log.error("Fail in persistence layer {}", e.getMessage());
                log.debug("Debug error", e);
                try {
                    transactionManager.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
