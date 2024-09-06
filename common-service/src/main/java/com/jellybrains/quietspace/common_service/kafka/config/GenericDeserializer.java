package com.jellybrains.quietspace.common_service.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

@Slf4j
public class GenericDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> type;

    public GenericDeserializer() {
    }

    public GenericDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        T object = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.info("exception at reading inputStream:{}", e.getMessage());
        } finally {
            try {
                byteArrayInputStream.close();
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                log.info("serialization exception:{}", e.getMessage());
            }
        }
        return object;
    }

    @Override
    public void close() {
        // Nothing to close
    }

}

