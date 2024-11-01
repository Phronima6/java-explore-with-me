package ru.practicum.main.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EWMMainServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EWMMainServiceApp.class, args);
    }

}