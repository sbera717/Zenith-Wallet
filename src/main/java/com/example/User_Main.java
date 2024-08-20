package com.example;

import com.example.Models.User;
import com.example.Repository.UserRepository;
import com.example.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class User_Main {
    public static void main(String[] args) {
        SpringApplication.run(User_Main.class,args);

    }
}