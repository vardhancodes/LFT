package com.lft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// Tell Spring Boot to skip looking for a database for now
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LftApplication {

    public static void main(String[] args) {
        SpringApplication.run(LftApplication.class, args);
        System.out.println("=== LFT Backend is up and running ===");
    }

}