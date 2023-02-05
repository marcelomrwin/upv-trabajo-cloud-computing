package es.upv.posgrado.test;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class DownloadImageTest {
    @Test
    public void testDownloadImage() throws IOException {
        URL url = new URL("https://wtop.com/wp-content/uploads/2022/11/Indonesia_Earthquake_23704-1024x683.jpg");
        String imageName = extractFileNameFromUrl(url);
        String tmpDir = System.getProperty("java.io.tmpdir");
        File imgTemp = new File(tmpDir+File.separator+imageName);
        System.out.println(imgTemp);
        imgTemp.delete();
    }

    protected String extractFileNameFromUrl(URL url) {
        String path = url.getPath();

        path = path.substring(path.lastIndexOf('/')+1, path.length());
        path = path.replaceAll(" ", "");
        path = path.toLowerCase();
        path = path.replaceAll(":", "");
        path = path.replaceAll("-", "_");
        path = path.replaceAll("%", "");

        return path;
    }
}
