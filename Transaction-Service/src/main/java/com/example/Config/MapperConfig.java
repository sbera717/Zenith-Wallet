package com.example.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }


    @Bean
    public JSONParser jsonParser(){
        return  new JSONParser();
    }

    @Bean
    public PasswordEncoder getPE(){
        return new BCryptPasswordEncoder();

    }


}
