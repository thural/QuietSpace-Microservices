package com.jellybrains.quietspace_backend_ms.authorization_service.configuration;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public <T> ReactiveKafkaProducerTemplate<String, T> reactiveKafkaProducerTemplate(
            KafkaProperties properties, SslBundles ssl) {
        Map<String, Object> props = properties.buildProducerProperties(ssl);
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Bean
    public <T> ReceiverOptions<String, T> kafkaReceiverOptions(SslBundles ssl, KafkaProperties kafkaProperties) {
        return ReceiverOptions.create(kafkaProperties.buildConsumerProperties(ssl));
    }

    @Bean
    public <T> ReactiveKafkaConsumerTemplate<String, T> reactiveKafkaConsumerTemplate(ReceiverOptions<String, T> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}
