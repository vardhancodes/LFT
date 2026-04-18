package com.lft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class LftApplication {
    public static void main(String[] args) {
        SpringApplication.run(LftApplication.class, args);
        System.out.println("=== LFT Backend is up and running ===");
    }
}
