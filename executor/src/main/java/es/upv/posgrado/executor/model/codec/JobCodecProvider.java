package es.upv.posgrado.executor.model.codec;

import es.upv.posgrado.common.model.Job;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class JobCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass.equals(Job.class)){
            return (Codec<T>) new JobCodec();
        }
        return null;
    }
}
