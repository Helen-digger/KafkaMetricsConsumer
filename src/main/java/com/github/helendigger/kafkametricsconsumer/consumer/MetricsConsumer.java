package com.github.helendigger.kafkametricsconsumer.consumer;

import com.github.helendigger.kafkametricsconsumer.configuration.kafka.KafkaProperties;
import com.github.helendigger.kafkametricsconsumer.dto.MetricDTO;
import com.github.helendigger.kafkametricsconsumer.service.MetricsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MetricsConsumer {
    public KafkaProperties properties;
    private MetricsService metricsService;
    @KafkaListener(topics = "#{@kafkaProperties.getMetricsTopicName()}",
            groupId = "#{@kafkaProperties.getConsumerGroupId()}")
    public void listen(ConsumerRecord<String, MetricDTO> record) {
        var serviceName = record.key();
        var serviceMetric = record.value();
        try {
            metricsService.addNewMetricRecord(serviceMetric, serviceName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
