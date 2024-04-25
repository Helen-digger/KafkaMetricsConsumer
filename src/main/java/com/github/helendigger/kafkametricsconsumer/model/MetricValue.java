package com.github.helendigger.kafkametricsconsumer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

/**
 * Entity to store values of a certain metric
 */
@Data
@Entity
public class MetricValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Double value;
    @NotNull
    private Instant timestamp;
    @NotNull
    @ManyToOne
    private Metric metric;
}
