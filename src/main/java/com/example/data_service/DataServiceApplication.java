package com.example.data_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;


@EnableCircuitBreaker
@SpringBootApplication
public class DataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataServiceApplication.class, args);
    }

}
