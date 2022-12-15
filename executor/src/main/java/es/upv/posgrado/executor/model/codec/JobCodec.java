package es.upv.posgrado.executor.model.codec;

import com.mongodb.MongoClientSettings;
import es.upv.posgrado.common.model.Job;
import es.upv.posgrado.common.model.JobStatus;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.LocalDateTime;
import java.util.UUID;

public class JobCodec implements CollectibleCodec<Job> {
    private final Codec<Document> documentCodec;

    public JobCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Job generateIdIfAbsentFromDocument(Job job) {
        if (!documentHasId(job)) {
            job.setId(UUID.randomUUID().toString());
        }
        return job;
    }

    @Override
    public boolean documentHasId(Job job) {
        return job.getId() != null && !"".equals(job.getId());
    }

    @Override
    public BsonValue getDocumentId(Job job) {
        return new BsonString(job.getId());
    }

    @Override
    public Job decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);
        Job job = Job.builder().build();

        if (document.getString(Job.ATTRIBUTE_ID) != null) {
            job.setId(document.getString(Job.ATTRIBUTE_ID));
        }
        job.setTitle(document.getString(Job.ATTRIBUTE_TITLE));
        job.setResult(document.getString(Job.ATTRIBUTE_RESULT));
        job.setStatus(JobStatus.valueOf(document.getString(Job.ATTRIBUTE_STATUS)));
        job.setProcessedAt(document.get(Job.ATTRIBUTE_PROCESSED_AT, LocalDateTime.class));
        job.setPublishedAt(document.get(Job.ATTRIBUTE_PUBLISHED_AT, LocalDateTime.class));
        job.setProcessedBy(document.getString(Job.ATTRIBUTE_PROCESSED_BY));

        return job;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Job job, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put(Job.ATTRIBUTE_TITLE, job.getTitle());
        doc.put(Job.ATTRIBUTE_RESULT, job.getResult());
        doc.put(Job.ATTRIBUTE_STATUS, job.getStatus());
        doc.put(Job.ATTRIBUTE_PROCESSED_AT, job.getProcessedAt());
        doc.put(Job.ATTRIBUTE_PUBLISHED_AT, job.getPublishedAt());
        doc.put(Job.ATTRIBUTE_PROCESSED_BY, job.getProcessedBy());
        documentCodec.encode(bsonWriter, doc, encoderContext);
    }

    @Override
    public Class<Job> getEncoderClass() {
        return Job.class;
    }
}
