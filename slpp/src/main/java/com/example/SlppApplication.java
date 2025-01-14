package com.example;


import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class SlppApplication {
    public static void main(String[] args) {
        SpringApplication.run(SlppApplication.class, args);
    }


}