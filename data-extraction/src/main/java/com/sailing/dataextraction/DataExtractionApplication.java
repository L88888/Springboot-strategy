package com.sailing.dataextraction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DataExtractionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataExtractionApplication.class, args);
    }

}
