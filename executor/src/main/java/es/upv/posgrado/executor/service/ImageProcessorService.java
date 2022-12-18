package es.upv.posgrado.executor.service;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;
import java.util.UUID;

@Singleton
@Slf4j
public class ImageProcessorService {

    public String convertImageToBase64String(String imageURL) throws IOException {
        URL url = new URL(imageURL);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        File imgTemp = File.createTempFile(UUID.randomUUID().toString(), ".file");

        FileOutputStream fileOutputStream = new FileOutputStream(imgTemp);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        String imageString = convertImageToByteArray(imgTemp);
        imgTemp.delete();
        return imageString;
    }

    private String convertImageToByteArray(File imgTemp) throws IOException {
        FileInputStream stream = new FileInputStream(imgTemp);
        int bufLength = 2048;
        byte[] buffer = new byte[bufLength];
        byte[] data;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int readLength;
        while ((readLength = stream.read(buffer, 0, bufLength)) != -1) {
            out.write(buffer, 0, readLength);
        }
        data = out.toByteArray();
        String imageString = Base64.getEncoder().withoutPadding().encodeToString(data);
        out.close();
        stream.close();
        return imageString;
    }

}
