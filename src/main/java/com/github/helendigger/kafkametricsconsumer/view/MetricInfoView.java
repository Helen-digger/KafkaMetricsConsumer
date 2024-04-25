package com.github.helendigger.kafkametricsconsumer.view;

/**
 * View interface that represents the overall metrics list
 */
public interface MetricInfoView {
    Long getId();
    String getName();
    String getServiceName();
}
