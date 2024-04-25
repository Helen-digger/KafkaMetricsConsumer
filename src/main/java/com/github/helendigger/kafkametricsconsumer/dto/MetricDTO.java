package com.github.helendigger.kafkametricsconsumer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Class that represents some application metric
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricDTO {
    @NotNull
    private String name;
    @NotNull
    private MetricTypeDTO type;
    @NotNull
    private Double value;
    @NotNull
    private Instant timestamp;
}
