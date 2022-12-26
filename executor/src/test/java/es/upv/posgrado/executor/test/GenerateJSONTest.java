package es.upv.posgrado.executor.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.executor.service.ExecutorService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

@QuarkusTest
public class GenerateJSONTest {

    @Inject
    ExecutorService executorService;

    @Test
    public void testJsonGenerator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Path pathJson = Path.of(this.getClass().getResource("/NewsSample.json").toURI());
        Path pathImage = Path.of(this.getClass().getResource("/indonesia-earthqauke.jpg").toURI());
        Map<?, ?> map = mapper.readValue(pathJson.toFile(), Map.class);
        Long id = Long.valueOf((String) map.get("id"));
        executorService.generateData(id, (String) map.get("title"), (String) map.get("description"), LocalDateTime.now(), LocalDateTime.now(), pathImage.toFile().toURI().toString());
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

}
