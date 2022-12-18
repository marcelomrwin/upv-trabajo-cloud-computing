package es.upv.posgrado.injector.service;

import es.upv.posgrado.client.shell.CommandExecutor;
import es.upv.posgrado.connectors.model.NewsDTO;
import es.upv.posgrado.injector.client.messaging.KafkaClient;
import es.upv.posgrado.injector.client.storage.MinioClient;
import es.upv.posgrado.injector.model.News;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class NewsProcessorService {

    @Inject
    KafkaClient kafkaClient;

    @Inject
    MinioClient minioClient;

    @Inject
    TransactionManager transactionManager;


    public void saveNewsFromArticle(Set<NewsDTO> newsDTOSet) {
        if (!newsDTOSet.isEmpty()) {
            for (NewsDTO newsDTO : newsDTOSet) {

                if (News.findByTitle(newsDTO.getTitle()).isPresent())
                    continue;

                News newsEntity = News.builder().title(newsDTO.getTitle()).description(newsDTO.getDescription())
                        .publishedAt(newsDTO.getPublishedAt()).generatedAt(LocalDateTime.now()).build();
                processNewEntity(newsDTO, newsEntity);
            }
        }
    }

    private void processNewEntity(NewsDTO newsDTO, News newsEntity) {
        String urlToImage = downloadArticleImage(newsDTO);
        if (urlToImage != null) {
            newsEntity.setUrlToImage(urlToImage);
            newsEntity.setThumbnail(newsDTO.getThumbnail());

            try {
                log.info("Proceeding to save News {}", newsEntity.getTitle());
                transactionManager.begin();
                newsEntity.persistAndFlush();
                transactionManager.commit();
                kafkaClient.notifyRecentNews(newsEntity).get(5, TimeUnit.SECONDS);
            } catch (PersistenceException e) {
                log.error("Fail in persistence layer {}", e.getMessage());
                log.debug("Debug error", e);
                try {
                    transactionManager.rollback();
                } catch (SystemException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String downloadArticleImage(NewsDTO newsDTO) {
        try {
            URL url = new URL(newsDTO.getUrlToImage());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            String imageName = extractFileNameFromUrl(url);
            File imgTemp = File.createTempFile(imageName, ".file");
            FileOutputStream fileOutputStream = new FileOutputStream(imgTemp);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            File uploadImage = new File(imgTemp.getParentFile(), "uploaded_" + imageName);
            CommandExecutor.executeCommand("convert", imgTemp.getAbsolutePath(), "-resize", "400x",
                    uploadImage.getAbsolutePath());

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

    private void generateImageThumbnail(NewsDTO newsDTO, String imageName, File imgTemp) {
        try {
            File thumbnail = new File(imgTemp.getParentFile(), "thumbnail_" + imageName);
            CommandExecutor.executeCommand("convert", imgTemp.getAbsolutePath(), "-resize", "75x",
                    thumbnail.getAbsolutePath());
            String imageBase64 = convertImageToByteArray(thumbnail);
            newsDTO.setThumbnail(imageBase64);
            thumbnail.delete();
        } catch (Exception e) {
            log.error("Fail creating thumbnail image in base64", e);
        }
    }

    private String extractFileNameFromUrl(URL url) throws NoSuchAlgorithmException {
        String path = url.getPath();
        return createMD5Hash(path);
    }

    private String createMD5Hash(final String input) throws NoSuchAlgorithmException {

        String hashtext = null;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());

        hashtext = convertToHex(messageDigest);

        return hashtext;
    }

    private String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
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
