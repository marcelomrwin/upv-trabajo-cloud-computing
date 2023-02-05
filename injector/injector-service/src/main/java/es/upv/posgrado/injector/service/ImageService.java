package es.upv.posgrado.injector.service;

import es.upv.posgrado.client.shell.CommandExecutor;
import es.upv.posgrado.common.model.NewsDTO;
import es.upv.posgrado.injector.client.storage.MinioClient;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@ApplicationScoped
@Slf4j
public class ImageService {

    @Inject
    MinioClient minioClient;

    @Inject
    Instance<CommandExecutor> commandExecutor;

    protected String downloadArticleImage(NewsDTO newsDTO) {
        try {
            URL url = new URL(newsDTO.getUrlToImage());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            String tmpDir = System.getProperty("java.io.tmpdir");
            String imageName = extractFileNameFromUrl(url);
            File imgTemp = new File(tmpDir+File.separator+imageName);
            FileOutputStream fileOutputStream = new FileOutputStream(imgTemp);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            File uploadImage = new File(imgTemp.getParentFile(), "uploaded_" + imageName);

            log.info("Image {} downloaded with success!! {}", url, uploadImage.getAbsolutePath());
            log.info("Resizing the image file");

            commandExecutor.get().executeCommand("convert", imgTemp.getAbsolutePath(), "-resize", "400x",
                    uploadImage.getAbsolutePath());

            log.info("Storing the image in minio");
            ObjectWriteResponse uploadObject = minioClient.uploadObject(imageName, uploadImage.getAbsolutePath());
            String imageURL = minioClient.getObjectPublicURL(imageName);

            generateImageThumbnail(newsDTO, imageName, imgTemp);
            imgTemp.delete();
            return imageURL;

        } catch (Exception e) {
            log.error("Fail to download the article image: {} \n{}", newsDTO.getUrlToImage(), e.getMessage());
        }
        return null;
    }

    protected void generateImageThumbnail(NewsDTO newsDTO, String imageName, File imgTemp) {
        try {
            File thumbnail = new File(imgTemp.getParentFile(), "thumbnail_" + imageName);
            commandExecutor.get().executeCommand("convert", imgTemp.getAbsolutePath(), "-resize", "75x",
                    thumbnail.getAbsolutePath());
            String imageBase64 = convertImageToByteArray(thumbnail);
            newsDTO.setThumbnail(imageBase64);
            thumbnail.delete();
        } catch (Exception e) {
            log.error("Fail creating thumbnail image in base64", e);
        }
    }

    protected String extractFileNameFromUrl(URL url) {
        String path = url.getPath();

        path = path.substring(path.lastIndexOf('/') + 1, path.length());
        path = path.replaceAll(" ", "");
        path = path.toLowerCase();
        path = path.replaceAll(":", "");
        path = path.replaceAll("-", "_");
        path = path.replaceAll("%", "");

        return path;
    }

    protected String createMD5Hash(final String input) throws NoSuchAlgorithmException {

        String hashtext = null;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());

        hashtext = convertToHex(messageDigest);

        return hashtext;
    }

    protected String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }

    protected String convertImageToByteArray(File imgTemp) throws IOException {
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
