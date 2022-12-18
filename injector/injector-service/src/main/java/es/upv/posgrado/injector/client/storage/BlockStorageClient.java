package es.upv.posgrado.injector.client.storage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@Slf4j
@ApplicationScoped
public class BlockStorageClient {
    @ConfigProperty(name = "minio.bucket")
    String newsBucket;

    @ConfigProperty(name = "minio.endpoint")
    String minioEndpoint;
    @ConfigProperty(name = "minio.user")
    String minioCredential;

    @ConfigProperty(name = "minio.password")
    String minioSecretKey;

    private MinioClient minioClient;

    private String getBucketPolicy() {
        return """
                {
                    "Statement": [
                        {
                            "Action": [
                                "s3:GetBucketLocation",
                                "s3:ListBucket"
                            ],
                            "Effect": "Allow",
                            "Principal": "*",
                            "Resource": "arn:aws:s3:::news"
                        },
                        {
                            "Action": "s3:GetObject",
                            "Effect": "Allow",
                            "Principal": "*",
                            "Resource": "arn:aws:s3:::news*"
                        }
                    ],
                    "Version": "2012-10-17"
                }""";
    }

    @Produces
    public MinioClient createMinioClient() {
        return minioClient;
    }

    void onStart(@Observes StartupEvent ev) {
        try {
            minioClient =
                    MinioClient.builder()
                            .endpoint(minioEndpoint)
                            .credentials(minioCredential, minioSecretKey)
                            .build();

            // Make newsBucket bucket if not exist.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(newsBucket).build());
            if (!found) {
                // Make a new bucket called 'newsBucket'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(newsBucket).build());
                log.info("Bucket " + newsBucket + " created");
            } else {
                log.info("Bucket " + newsBucket + " already exists");
            }
            //make news public
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(newsBucket).config(getBucketPolicy()).build());


        } catch (Exception e) {
            log.error("Fail in initialize minio newsBucket ");
        }
    }

}
