package es.upv.posgrado.injector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.common.model.RecentNewsDTO;
import es.upv.posgrado.connectors.model.NewsDTO;
import es.upv.posgrado.injector.model.News;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class NewsProcessorService {

    @ConfigProperty(name = "minio.endpoint")
    String minioEndpoint;
    @ConfigProperty(name = "minio.bucket")
    String newsBucket;

    @ConfigProperty(name = "app.kafka.topic.name", defaultValue = "recent-news")
    String topicName;
    @Inject
    MinioClient minioClient;

    @Inject
    Producer<String, String> producer;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    public void saveNewsFromArticle(Set<NewsDTO> newsDTOSet) {
        if (!newsDTOSet.isEmpty()) {
            for (NewsDTO newsDTO : newsDTOSet) {
                News newsEntity = News.builder().title(newsDTO.getTitle()).description(newsDTO.getDescription()).publishedAt(newsDTO.getPublishedAt()).generatedAt(LocalDateTime.now()).build();
                String urlToImage = downloadArticleImage(newsDTO.getUrlToImage());
                if (urlToImage != null) {
                    newsEntity.setUrlToImage(urlToImage);
                    log.info("Proceeding to save News {}", newsEntity.getTitle());
                    newsEntity.persist();
                    RecentNewsDTO recentNewsDTO = RecentNewsDTO.builder().id(newsEntity.id).title(newsEntity.getTitle()).publishedAt(newsEntity.getPublishedAt()).build();
                    try {
                        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, String.valueOf(recentNewsDTO.getId()), objectMapper.writeValueAsString(recentNewsDTO));
                        producer.send(record,(recordMetadata,e)->{
                            if (e==null){
                                // the record was successfully sent
                                log.info("Received new metadata. \n" +
                                        "Topic:" + recordMetadata.topic() + "\n" +
                                        "Key:" + record.key() + "\n" +
                                        "Partition: " + recordMetadata.partition() + "\n" +
                                        "Offset: " + recordMetadata.offset() + "\n" +
                                        "Timestamp: " + recordMetadata.timestamp());
                            }else{
                                log.error("Error while producing message to kafka cluster",e);
                            }
                        });

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        producer.flush();
                    }
                }
            }
        }
    }

    private String downloadArticleImage(String urlToImage) {
        try {
            URL url = new URL(urlToImage);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            String imageName = extractFileNameFromUrl(url);
            File imgTemp = File.createTempFile(imageName, ".file");
            FileOutputStream fileOutputStream = new FileOutputStream(imgTemp);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            ObjectWriteResponse uploadObject = minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(newsBucket)
                            .object(imageName)
                            .filename(imgTemp.getAbsolutePath())
                            .build());
            String imageURL = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(newsBucket)
                            .object(imageName)
                            .expiry(7, TimeUnit.DAYS)
                            .method(Method.GET)
                            .build());
            imgTemp.delete();
            return imageURL;

        } catch (Exception e) {
            log.error("Fail to download the article image\n" + urlToImage, e);
        }
        return null;
    }

    private String extractFileNameFromUrl(URL url) throws NoSuchAlgorithmException {
        String path = url.getPath();
        return createMD5Hash(path);
    }

    private String createMD5Hash(final String input)
            throws NoSuchAlgorithmException {

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

}
