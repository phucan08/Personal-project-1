package com.example.autoparts;

import com.example.autoparts.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AutopartsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutopartsApplication.class, args);
    }

    @Bean
    public CommandLineRunner createDefaultAdmin(UserService userService) {
        return args -> {
            userService.createDefaultAdminIfNotExists();
        };
    }
}