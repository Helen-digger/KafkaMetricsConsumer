package com.github.helendigger.kafkametricsconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaMetricsConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaMetricsConsumerApplication.class, args);
    }

}
