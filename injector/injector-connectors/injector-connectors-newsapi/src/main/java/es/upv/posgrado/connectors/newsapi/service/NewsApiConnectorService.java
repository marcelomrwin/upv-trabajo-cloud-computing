package es.upv.posgrado.connectors.newsapi.service;

import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.connectors.newsapi.client.NewsApiEverythingService;
import es.upv.posgrado.connectors.newsapi.model.ArticleResponse;
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
public class NewsApiConnectorService implements NewsService {

    @ConfigProperty(name = "newsapi.apikey")
    String apiKey;
    @Inject
    @RestClient
    NewsApiEverythingService newsApiEverythingService;

    private int page = 1;

    @Override
    @Timeout(value = 5,unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 1)
    @CircuitBreaker(requestVolumeThreshold = 4)
    public Set<NewsDTO> getNews() {
        Set<NewsDTO> newsDTOSet = new HashSet<>();
        try {
            ArticleResponse articleResponse = newsApiEverythingService.getEverythingNews(apiKey, "(apple OR ibm OR google OR \"red hat\")", "en", "2022-12-10", "publishedAt", "100", String.valueOf(page));
            page++;
            articleResponse.getArticles().parallelStream().forEach(article -> {
                if (Objects.nonNull(article.getUrlToImage())) {
                    NewsDTO newsDTO = NewsDTO.builder().title(article.getTitle()).description(article.getDescription()).publishedAt(LocalDateTime.parse(article.getPublishedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)).urlToImage(article.getUrlToImage()).build();
                    newsDTOSet.add(newsDTO);
                }
            });

        } catch (Exception e) {
            page=1;
            log.error("Fail getting news from NewsAPI\n{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return newsDTOSet;
    }
}
