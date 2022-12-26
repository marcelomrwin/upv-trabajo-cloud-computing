package es.upv.posgrado.test;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLResolverTest {

    String urlToImage = "https://techcrunch.com/wp-content/uploads/2022/12/lensa-ai.jpg?resize=1200,417";

    @Test
    public void urlTest() throws MalformedURLException, URISyntaxException {
        URL url = new URL(urlToImage);
        String path = url.getPath();
        path = path.substring(path.lastIndexOf('/') + 1);
        String prefix = path.substring(0, path.lastIndexOf('.'));
        String suffix = path.substring(path.lastIndexOf('.')+1);
        System.out.println(path);
        System.out.println(prefix);
        System.out.println(suffix);
    }
}
