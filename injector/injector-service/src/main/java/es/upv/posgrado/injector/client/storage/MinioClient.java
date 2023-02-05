package es.upv.posgrado.injector.client.storage;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Retention;
import io.minio.messages.RetentionDurationDays;
import io.minio.messages.RetentionMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class MinioClient {
    @ConfigProperty(name = "minio.bucket")
    String newsBucket;
    @Inject
    io.minio.MinioClient minioClient;

    public ObjectWriteResponse uploadObject(String objectName,String objectPath) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ObjectWriteResponse uploadObject = minioClient.uploadObject(UploadObjectArgs.builder().bucket(newsBucket)
                .object(objectName).filename(objectPath)
                .build());
        return uploadObject;
    }

    public String getObjectPublicURL(String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String objectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(newsBucket)
                .object(objectName).expiry(7, TimeUnit.DAYS).method(Method.GET).build());

        return objectUrl;
    }
}
