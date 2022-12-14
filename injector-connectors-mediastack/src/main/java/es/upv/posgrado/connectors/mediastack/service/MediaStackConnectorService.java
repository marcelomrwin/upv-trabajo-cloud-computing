package es.upv.posgrado.connectors.mediastack.service;

import es.upv.posgrado.connectors.mediastack.client.MediaStackNewsApiService;
import es.upv.posgrado.connectors.mediastack.model.MediaStackNewsResponse;
import es.upv.posgrado.connectors.model.NewsDTO;
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
public class MediaStackConnectorService implements NewsService {

    @ConfigProperty(name = "mediastack.apikey")
    String apiKey;
    @Inject
    @RestClient
    MediaStackNewsApiService mediaStackNewsApiService;

    private int offset=0;

    @Override
    @Timeout(value = 5,unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 2)
    @CircuitBreaker(requestVolumeThreshold = 4)
    public Set<NewsDTO> getNews() {
        Set<NewsDTO> newsDTOSet = new HashSet<>();
        try {
            log.info("Search term {} with offset {} and limit {}","java",offset,100);
            MediaStackNewsResponse response = mediaStackNewsApiService.get(apiKey, "en", "java", "100", String.valueOf(offset));
            offset++;
            response.getData().parallelStream().forEach(mediaStackNews -> {
                if (Objects.nonNull(mediaStackNews.getImage())) {
                    NewsDTO newsDTO = NewsDTO.builder().title(mediaStackNews.getTitle()).description(mediaStackNews.getDescription()).publishedAt(LocalDateTime.parse(mediaStackNews.getPublished_at(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)).urlToImage(mediaStackNews.getImage()).build();
                    newsDTOSet.add(newsDTO);
                }
            });

        } catch (Exception e) {
            offset=0;
            log.error("Fail getting news from MediaStack", e);
            throw new RuntimeException(e);
        }
        return newsDTOSet;
    }
}
