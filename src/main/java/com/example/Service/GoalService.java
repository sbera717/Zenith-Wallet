package com.example.Service;

import com.example.DTO.Interest;
import com.example.DTO.UserGoalCreation;
import com.example.Models.Goal;
import com.example.Models.GoalTxnStatus;
import com.example.Repository.GoalRepository;
import com.example.Utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONParser jsonParser;


    public Goal createGoal(UserGoalCreation userGoalCreation,String getUsername) throws JsonProcessingException {

        LocalDate date = LocalDate.now();
        LocalDate futureDate = date.plus(10, ChronoUnit.MONTHS);
        Goal goal = userGoalCreation.to();
        goal.setUsername(getUsername);
        goal.setMatureDate(futureDate);
        goal.setGoalTxnStatus(GoalTxnStatus.PENDING);


        JSONObject event = objectMapper.convertValue(goal, JSONObject.class);
        String goalMsg = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(Constants.Goal_Wallet_Create,goalMsg);

        return goalRepository.save(goal);

    }

    public Goal getGoal(String username){
        return goalRepository.findByUsername(username);

    }

    public void deleteGoal(String username){
        Goal goal = goalRepository.findByUsername(username);
        kafkaTemplate.send(Constants.Delete_Goal_Account,username);
        goalRepository.deleteById(goal.getGoalId());
    }


    @KafkaListener(topics = Constants.Goal_User_Update,groupId = "Goal_User1",errorHandler = "kafkaHandler")
    public void updateGoal(String incomingMsgFromTxn) throws ParseException, JsonProcessingException {
        JSONObject event = (JSONObject) jsonParser.parse(incomingMsgFromTxn);
        String username = (String) event.get("sender");
        Goal goal = goalRepository.findByUsername(username);
        Interest interest = new Interest(username,goal.getMonthlyAmount());
        JSONObject eventInterest = objectMapper.convertValue(interest, JSONObject.class);
        String interestWallet = objectMapper.writeValueAsString(eventInterest);
        String transactionStatus = String.valueOf(event.get("transactionStatus"));
        if(!transactionStatus.equals("FAILED")){
            int duration = goal.getDuration() - 1;
            goal.setMonthsRemaining(duration);
            goal.setLastAttemptedDate(LocalDate.now());
            goal.setGoalTxnStatus(GoalTxnStatus.SUCCESS);
            goalRepository.save(goal);
            kafkaTemplate.send(Constants.Goal_Add_Interest,interestWallet);
            JSONObject jsonObject = objectMapper.convertValue(goal, JSONObject.class);
            kafkaTemplate.send(Constants.Interest_Done,objectMapper.writeValueAsString(jsonObject));
        }else{
            goal.setLastAttemptedDate(LocalDate.now());
            goal.setGoalTxnStatus(GoalTxnStatus.FAILED);
            goalRepository.save(goal);
            JSONObject jsonObject = objectMapper.convertValue(goal, JSONObject.class);
            kafkaTemplate.send(Constants.Interest_Done,objectMapper.writeValueAsString(jsonObject));

        }
    }




}
