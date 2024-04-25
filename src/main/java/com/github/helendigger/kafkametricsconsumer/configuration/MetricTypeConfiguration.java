package com.github.helendigger.kafkametricsconsumer.configuration;

import com.github.helendigger.kafkametricsconsumer.dto.MetricTypeDTO;
import com.github.helendigger.kafkametricsconsumer.model.MetricType;
import com.github.helendigger.kafkametricsconsumer.repository.MetricTypeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MetricTypeConfiguration {
    private MetricTypeRepository metricTypeRepository;
    @PostConstruct
    @Transactional
    public void fillMetricTypes() {
        for (var type: MetricTypeDTO.values()) {
            var metricType = metricTypeRepository.findMetricTypeByName(type.name());
            if (metricType.isEmpty()) {
                MetricType newType = new MetricType();
                newType.setName(type.name());
                metricTypeRepository.saveAndFlush(newType);
            }
        }
    }
}
