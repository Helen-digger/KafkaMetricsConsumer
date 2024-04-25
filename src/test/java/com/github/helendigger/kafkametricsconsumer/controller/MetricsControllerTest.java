package com.github.helendigger.kafkametricsconsumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.helendigger.kafkametricsconsumer.controller.configuration.MetricsControllerConfiguration;
import com.github.helendigger.kafkametricsconsumer.dto.MetricDTO;
import com.github.helendigger.kafkametricsconsumer.dto.MetricTypeDTO;
import com.github.helendigger.kafkametricsconsumer.service.MetricsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@SpringBootTest(classes = MetricsControllerConfiguration.class)
public class MetricsControllerTest {
    @Autowired
    MetricsController metricsController;
    @Autowired
    MetricsService service;
    @Autowired
    ObjectMapper mapper;
    @Test
    public void testGetById() {
        var dto = MetricDTO.builder()
                .value(100.0)
                .type(MetricTypeDTO.COUNTER)
                .name("example-metric")
                .timestamp(Instant.now()).build();
        var value = service.addNewMetricRecord(dto, "example-service");
        Assertions.assertNotNull(value);
        var rate = metricsController.getMetricById(value.getId());
        Assertions.assertEquals(HttpStatus.OK, rate.getStatusCode());
        Assertions.assertNotNull(rate.getBody());
        Assertions.assertEquals(dto.getName(), rate.getBody().getName());
    }

    @Test
    public void testGetByIdNotFound() {
        var rate = metricsController.getMetricById(3000L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, rate.getStatusCode());
        Assertions.assertNull(rate.getBody());
    }

    @Test
    public void testGetAll() {
        var allMetrics = metricsController.getAllMetrics();
        Assertions.assertEquals(HttpStatus.OK, allMetrics.getStatusCode());
    }
}
