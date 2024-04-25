package com.github.helendigger.kafkametricsconsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricViewDTO {
    private String name;
    private Double rate;
    private Double max;
}
