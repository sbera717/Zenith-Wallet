package com.example.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {


    @Bean
    public JSONParser jsonParser(){
        return  new JSONParser();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
