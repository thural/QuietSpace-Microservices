package com.jellybrains.quietspace.common_service.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

@Slf4j
public class GenericSerializer<T> implements Serializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public byte[] serialize(String topic, T data) {
        byte[] serializedBytes = null;
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            serializedBytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.info("exception creating outputStream: {}", e.getMessage());
        } finally {
            try {
                byteArrayOutputStream.close();
                if (objectOutputStream != null) objectOutputStream.close();
            } catch (IOException e) {
                log.info("exception during serialization: {}", e.getMessage());
            }
        }
        return serializedBytes;
    }

    @Override
    public void close() {
        // Nothing to close
    }
}