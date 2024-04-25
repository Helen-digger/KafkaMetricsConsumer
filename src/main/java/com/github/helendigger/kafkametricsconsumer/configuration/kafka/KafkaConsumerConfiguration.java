package com.github.helendigger.kafkametricsconsumer.configuration.kafka;

import com.github.helendigger.kafkametricsconsumer.dto.MetricDTO;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@AllArgsConstructor
public class KafkaConsumerConfiguration {
    private KafkaProperties kafkaProperties;
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MetricDTO>>
        kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, MetricDTO>();
        factory.setConsumerFactory(consumerFactory(new StringDeserializer(),
                new JsonDeserializer<>(MetricDTO.class, false)));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.getContainerProperties().setPollTimeout(kafkaProperties.getPollTimeoutMillis());
        return factory;
    }

    private <K,V> ConsumerFactory<K, V> consumerFactory(Deserializer<K> key, Deserializer<V> value) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), key, value);
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }
}
