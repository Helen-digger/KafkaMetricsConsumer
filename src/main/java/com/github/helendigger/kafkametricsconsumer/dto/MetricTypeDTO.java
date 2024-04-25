package com.github.helendigger.kafkametricsconsumer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * Metric type used in DTO metric receiver
 */
public enum MetricTypeDTO {
    COUNTER,
    GAUGE;

    @JsonCreator
    public static MetricTypeDTO create(String value) {
        return Arrays.stream(values()).filter(type -> type.name().equals(value))
                .findFirst()
                .orElseThrow();
    }
}
