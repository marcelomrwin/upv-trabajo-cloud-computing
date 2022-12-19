package es.upv.posgrado.connectors.newsdata.service;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.connectors.newsdata.client.NewsDataApiService;
import es.upv.posgrado.connectors.newsdata.model.NewsDataResponse;
import es.upv.posgrado.connectors.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class NewsDataConnectorService implements NewsService {

    @ConfigProperty(name = "newsdata.apikey")
    String apiKey;
    @Inject
    @RestClient
    NewsDataApiService newsDataApiService;

    @Override
    @Timeout(value = 5,unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 1)
    @CircuitBreaker(requestVolumeThreshold = 4)
    public Set<NewsDTO> getNews() {
        Set<NewsDTO> newsDTOSet = new HashSet<>();
        try {

            NewsDataResponse response = newsDataApiService.get(apiKey,"en");

            response.getResults().parallelStream().forEach(news -> {
                if (Objects.nonNull(news.getImage_url())) {
                    NewsDTO newsDTO = NewsDTO.builder().title(news.getTitle()).description(news.getDescription()).publishedAt(LocalDateTime.parse(news.getPubDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).urlToImage(news.getImage_url()).build();
                    newsDTOSet.add(newsDTO);
                }
            });

        } catch (Exception e) {
            log.error("Fail getting news from NewsDAta\n{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return newsDTOSet;
    }
}
