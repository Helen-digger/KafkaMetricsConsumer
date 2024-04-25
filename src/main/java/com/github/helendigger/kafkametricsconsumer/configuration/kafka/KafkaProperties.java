package com.github.helendigger.kafkametricsconsumer.configuration.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.common")
@Getter
@Setter
public class KafkaProperties {
    private String bootstrapServers;
    private String metricsTopicName;
    private String consumerGroupId;
    private Long pollTimeoutMillis;
}
