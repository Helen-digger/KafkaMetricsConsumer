package com.github.helendigger.kafkametricsconsumer.service;

import com.github.helendigger.kafkametricsconsumer.dto.MetricDTO;
import com.github.helendigger.kafkametricsconsumer.dto.MetricViewDTO;
import com.github.helendigger.kafkametricsconsumer.exception.InvalidMetricRecordException;
import com.github.helendigger.kafkametricsconsumer.model.Metric;
import com.github.helendigger.kafkametricsconsumer.model.MetricServiceEntity;
import com.github.helendigger.kafkametricsconsumer.model.MetricType;
import com.github.helendigger.kafkametricsconsumer.model.MetricValue;
import com.github.helendigger.kafkametricsconsumer.repository.MetricRepository;
import com.github.helendigger.kafkametricsconsumer.repository.MetricTypeRepository;
import com.github.helendigger.kafkametricsconsumer.repository.MetricValueRepository;
import com.github.helendigger.kafkametricsconsumer.repository.ServiceRepository;
import com.github.helendigger.kafkametricsconsumer.view.MetricInfoView;
import com.github.helendigger.kafkametricsconsumer.view.MetricView;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MetricsService {
    private MetricRepository metricRepository;
    private MetricValueRepository metricValueRepository;
    private ServiceRepository serviceRepository;
    private MetricTypeRepository metricTypeRepository;

    public List<MetricInfoView> getAllMetrics() {
        return metricRepository.getAllMetrics();
    }

    @Transactional
    public MetricValue addNewMetricRecord(MetricDTO metricDTO, String sourceService) {
        var metricType = getMetricType(metricDTO.getType().name());
        var serviceEntity = getService(sourceService);
        var metric = getMetric(metricDTO.getName(), metricType, serviceEntity);

        var newValue = new MetricValue();
        newValue.setMetric(metric);
        newValue.setTimestamp(metricDTO.getTimestamp());
        newValue.setValue(metricDTO.getValue());
        return metricValueRepository.saveAndFlush(newValue);
    }

    public Optional<MetricViewDTO> getMetricStatisticById(Long id) {
        var metric = metricRepository.findById(id);
        if (metric.isEmpty()) {
            return Optional.empty();
        }
        var stats = metricValueRepository.getMetricRate(id);
        var dto = new MetricViewDTO();
        metric.map(Metric::getName).ifPresent(dto::setName);
        stats.map(MetricView::getRate).ifPresentOrElse(dto::setRate, () -> dto.setRate(0.0));
        stats.map(MetricView::getMax).ifPresentOrElse(dto::setMax, () -> dto.setMax(0.0));
        return Optional.of(dto);
    }


    private MetricType getMetricType(String metricName) {
        var metricTypeEntity = metricTypeRepository.findMetricTypeByName(metricName);
        if (metricTypeEntity.isEmpty()) {
            throw new InvalidMetricRecordException("Metric type is not found");
        }
        return metricTypeEntity.get();
    }

    private MetricServiceEntity getService(String serviceName) {
        var service = serviceRepository.findByName(serviceName);
        if (service.isEmpty()) {
            MetricServiceEntity newService = new MetricServiceEntity();
            newService.setName(serviceName);
            return serviceRepository.saveAndFlush(newService);
        }
        return service.get();
    }

    private Metric getMetric(String name, MetricType type, MetricServiceEntity service) {
        var metric = metricRepository.findMetricByNameAndMetricTypeAndMetricServiceEntity(name,
                type, service);
        if (metric.isEmpty()) {
            var newMetric = new Metric();
            newMetric.setMetricType(type);
            newMetric.setMetricServiceEntity(service);
            newMetric.setName(name);
            return metricRepository.saveAndFlush(newMetric);
        }
        return metric.get();
    }
}
