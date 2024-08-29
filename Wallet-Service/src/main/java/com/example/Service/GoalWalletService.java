package com.example.Service;

import com.example.DTO.TxnGoal;
import com.example.Models.GoalWallet;
import com.example.Repository.GoalWalletRepository;
import com.example.Utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class GoalWalletService {

    private static Logger logger = LoggerFactory.getLogger(GoalWalletService.class);

    @Autowired
    private GoalWalletRepository goalWalletRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JSONParser jsonParser;

    @KafkaListener(topics = {Constants.Goal_Wallet_Create},groupId = "Goal_wallet1",errorHandler = "kafkaHandler")
    public void create(String msg) throws ParseException, JsonProcessingException {
        System.out.println(msg);
        JSONObject event = (JSONObject) jsonParser.parse(msg);
        String username = (String) event.get("username");
        Double amount = (Double) event.get("monthlyAmount");
        String goal = (String) event.get("goal");
        if(username == null || goal == null){
            logger.warn("create: unable to find username or goal in the event, data = {}",event);
            return;
        }

        GoalWallet goalWallet = GoalWallet.builder()
                .balance(0.0)
                .username(username)
                .build();


        TxnGoal txn = new TxnGoal(username,amount);

        JSONObject jsonObject = objectMapper.convertValue(txn, JSONObject.class);
        String details = objectMapper.writeValueAsString(jsonObject);
        kafkaTemplate.send(Constants.Goal_Wallet_Update,details);

        JSONObject jsonObject1 = objectMapper.convertValue(goalWallet, JSONObject.class);
        kafkaTemplate.send(Constants.New_Goal_Account,objectMapper.writeValueAsString(jsonObject1));
        goalWalletRepository.save(goalWallet);

    }

    @KafkaListener(topics = Constants.Goal_Add_Interest,groupId = "Goal_Add1",errorHandler = "kafkaHandler")
    @Transactional
    public void updateGoalBalanceWithInterest(String incomingDetailsFromUser) throws ParseException {
        System.out.println(incomingDetailsFromUser);
        JSONObject eventInterest = (JSONObject) jsonParser.parse(incomingDetailsFromUser);
        String username = (String) eventInterest.get("username");
        Double monthlyAmount = (double) eventInterest.get("amount");
        GoalWallet goalWallet = goalWalletRepository.findByUsername(username);
        //if(!goalWallet.getUpdatedOn().equals(LocalDate.now())){
            Double totalAmount = goalWallet.getBalance();
            Double totalAmountWithInterest = calculateCumulativeInterest(totalAmount,monthlyAmount,(double) 5);
            goalWallet.setBalance(totalAmountWithInterest);
            goalWallet.setUpdatedOn(LocalDate.now());
            goalWalletRepository.save(goalWallet);
        //}

    }

    @KafkaListener(topics = Constants.Return_Goal_Money,groupId = "Return_Goal1",errorHandler = "kafkaHandler")
    public void ReturningMoneyToUser(String msg) throws JsonProcessingException {
        Double balance = goalWalletRepository.findGoalBalance(msg);
        logger.warn("{}",balance);
        TxnGoal txn = new TxnGoal(msg,balance);
        JSONObject jsonObject = objectMapper.convertValue(txn, JSONObject.class);
        String sendMsg =  objectMapper.writeValueAsString(jsonObject);
        kafkaTemplate.send(Constants.Return_Goal_Money_Init,sendMsg);
    }

    @KafkaListener(topics = Constants.Final_Goal_Balance,groupId = "Final_Goal1",errorHandler = "kafkaHandler")
    @Transactional
    public void updateGoalWallet(String username){
        GoalWallet goalWallet = goalWalletRepository.findByUsername(username);
        goalWallet.setBalance(0.0);
        goalWalletRepository.save(goalWallet);
    }


    public Double calculateCumulativeInterest(Double principal, Double monthlyDebit, Double monthlyRatePercent) {
        Double monthlyRate = monthlyRatePercent / 100;
        Double accumulatedAmount = principal;
        accumulatedAmount += monthlyDebit;
        Double interest = accumulatedAmount * monthlyRate;
        accumulatedAmount += interest;
        return accumulatedAmount;
    }

    public Double getGoalBalance(String username){
        GoalWallet goal = goalWalletRepository.findByUsername(username);
        return  goal.getBalance();
    }

    @KafkaListener(topics = Constants.Delete_Goal_Account,groupId = "Return_Goal1",errorHandler = "kafkaHandler")
    public void deleteGoalWallet(String username) throws JsonProcessingException {
        ReturningMoneyToUser(username);
    }
}
