package es.upv.posgrado.api.test;

import es.upv.posgrado.api.model.HotNews;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.net.URL;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@Tag("integration")
@Slf4j
public class SubmitJobTest {

    public static final long HOTNEWS_ID = 1000L;

    @TestHTTPResource("/api/jobs")
    URL jobsEndpoint;

    @BeforeEach
    void setUp() {
        MockedStatic<PanacheEntityBase> mock = Mockito.mockStatic(PanacheEntityBase.class);
        mock.when(() -> PanacheEntityBase.findById(HOTNEWS_ID)).thenReturn(HotNews.builder().id(HOTNEWS_ID).title("Test title").publishedAt(LocalDateTime.now()).build());
    }

    @Test
    void whenFindByMockId_thenHotnewsShouldBeFound() {
        assertEquals(HOTNEWS_ID, ((HotNews) PanacheEntityBase.findById(HOTNEWS_ID)).getId());
    }

    @Test
    void whenSubmitJobWithValidId_thenJobShouldBeCreated() {
        String response = given().formParam("id", 1000L).when().post(jobsEndpoint).then().statusCode(Response.Status.CREATED.getStatusCode()).extract().body().asPrettyString();
        log.info("Response \n{}", response);
    }

}
