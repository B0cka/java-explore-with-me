package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class StatsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsServerApplication.class, args);
    }

}