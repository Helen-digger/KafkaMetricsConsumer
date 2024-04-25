package com.github.helendigger.kafkametricsconsumer.exception;

/**
 * Errors while reading metrics from consumer and trying to accumulate them
 */
public class InvalidMetricRecordException extends RuntimeException{
    public InvalidMetricRecordException(String message) {
        super(message);
    }
}
