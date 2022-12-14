package es.upv.posgrado.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.executor.model.JobDTO;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class ExecutorActor {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ScriptExecutor scriptExecutor;

    public void generateData(Long id, String title, String description, LocalDateTime generatedAt, LocalDateTime publishedAt, String imageURL) throws Exception {
        JobDTO jobDTO = JobDTO.builder().id(id).title(title).description(description)
                .generatedAt(generatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .publishedAt(publishedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build();
        jobDTO.setImage(convertImageToBase64String(imageURL));
        File f = File.createTempFile("file", ".json");
        Files.writeString(f.toPath(), objectMapper.writeValueAsString(jobDTO));
        log.info("calling python script");
        String html = scriptExecutor.executeScriptCommand(f.getAbsolutePath());
        f.delete();
        //publish html to kafka as result of job
        log.info(html);
    }

    private String convertImageToBase64String(String imageURL) throws IOException {
        URL url = new URL(imageURL);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        File imgTemp = File.createTempFile(UUID.randomUUID().toString(), ".file");
        FileOutputStream fileOutputStream = new FileOutputStream(imgTemp);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

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
        imgTemp.delete();
        return imageString;
    }

}
