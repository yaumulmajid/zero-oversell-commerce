package com.zerooversell.expiry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpiryWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpiryWorkerApplication.class, args);
    }
}
