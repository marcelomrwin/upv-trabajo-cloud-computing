package es.upv.posgrado.test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import es.upv.posgrado.client.shell.CommandExecutor;

public class ImageResizeTest {

    private static void printImageSize(Path pathImage) throws IOException {
        long bytesSize = Files.size(pathImage);
        BigDecimal bd = BigDecimal.valueOf(bytesSize);
        System.out.println("Size in bytes " + bytesSize);
        System.out.println("Size in Kbytes " + bd.divide(BigDecimal.valueOf(1024)).setScale(2, RoundingMode.HALF_EVEN));
        System.out.println("Size in Mbytes " + bd.divide(BigDecimal.valueOf(1024 * 1024)).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    public void printEnvSys(){
        System.out.println(System.getProperty("java.io.tmpdir"));
    }

    @Test
    public void testResizeImage() throws URISyntaxException, IOException, ExecutionException, InterruptedException, TimeoutException {
        String fileName = "big_image.png";
        String newFileName = System.getProperty("java.io.tmpdir")+File.separator+ "resized_" + fileName;
        Path pathImage = Path.of(this.getClass().getResource("/" + fileName).toURI());
        printImageSize(pathImage);

        String commandExit = new CommandExecutor().executeCommand("convert",pathImage.toAbsolutePath().toString(),"-resize","75x",newFileName);
        System.out.println("Console output: "+ commandExit);
        File resizedImage = new File(newFileName);

        System.out.println(resizedImage.getAbsolutePath() + " created");
        printImageSize(resizedImage.toPath());

    }


}
