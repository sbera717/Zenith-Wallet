package com.example.Service;

import com.example.DTO.Interest;
import com.example.Repository.GoalRepository;
import com.example.Utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CronService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Scheduled(cron = "0 39 12 * * *")
    public void deductAmountOn1st() throws JsonProcessingException, InterruptedException {
        List<Interest> allUser = goalRepository.deductAmountPerMonthOn1st();
        System.out.println("users" + allUser);
        for(Interest interest : allUser){
            JSONObject event = objectMapper.convertValue(interest, JSONObject.class);
            String interest1st = objectMapper.writeValueAsString(event);
            Thread.sleep(2000);
            //kafkaTemplate.send(Constants.Goal_Wallet_Update,interest1st);


        }
    }

    @Scheduled(cron = "0 19 15 * * *")
    public void deductAmountOn5th() throws JsonProcessingException, InterruptedException {
        List<Interest> allUser = goalRepository.deductAmountPerMonthOn5th();
        System.out.println("user " + allUser);
        for (Interest interest : allUser) {
            JSONObject event = objectMapper.convertValue(interest, JSONObject.class);
            String interest5th = objectMapper.writeValueAsString(event);
            Thread.sleep(2000);
            //kafkaTemplate.send(Constants.Goal_Wallet_Update, interest5th);

        }
    }

    @Scheduled(cron = "0 43 11 * * *")
    public void returnMoneyToUserWithCredit() throws JsonProcessingException, InterruptedException {
        List<String> allUser = goalRepository.findUserToCreditAmountWithInterest(LocalDate.now());
        System.out.println("user" + allUser);
        for (String user : allUser) {
            kafkaTemplate.send(Constants.Return_Goal_Money, user);
            Thread.sleep(2000);
        }
    }







}
