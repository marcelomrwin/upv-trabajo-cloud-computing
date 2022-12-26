package es.upv.posgrado.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.injector.model.News;
import es.upv.posgrado.injector.service.NewsProcessorService;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

@Tag("integration")
@QuarkusTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsProcessorServiceTest {

    static Set<NewsDTO> newsDTOS;
    @Inject
    NewsProcessorService processorService;

    @BeforeAll
    static void prepare() throws URISyntaxException, IOException {
        log.info("Loading all news from json file");
        Path json = Path.of(NewsProcessorServiceTest.class.getResource("/" + "news-test.json").toURI());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        newsDTOS = objectMapper.readValue(json.toFile(), new TypeReference<Set<NewsDTO>>() {
        });
    }

    @BeforeEach
    @Transactional
    public void before(){
        log.info("Removing all news used by test");
        newsDTOS.forEach(newsDTO -> {
            long deletedKey = News.delete("delete from News n where n.title = ?1", newsDTO.getTitle());
            log.info("Key {} was deleted from the database", deletedKey);
        });
    }

    @Test
    public void processNews() {
        processorService.saveNewsFromArticle(newsDTOS);
    }

}
