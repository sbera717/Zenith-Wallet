package com.example.Service;

import com.example.DTO.CreateTransactionRequest;
import com.example.Models.Transaction;
import com.example.Models.TransactionStatus;
import com.example.Repository.TxnRepository;
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

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JSONParser jsonParser;

    @Autowired
    TxnRepository txnRepository;

    private static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @KafkaListener(topics = Constants.Goal_Wallet_Update,groupId = "Goal_Update1",errorHandler = "kafkaHandler")
    public void initiateTxnGoal(String msg) throws ParseException, JsonProcessingException {
        JSONObject event = (JSONObject) jsonParser.parse(msg);
        String sender = (String) event.get("username");
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setReceiver("1111155555");
        double amount = (double) event.get("amount");
        createTransactionRequest.setAmount(amount);
        createTransactionRequest.setPurpose("Transferring to Goal Deposit");

        initiateTxn(sender,createTransactionRequest);
    }

    @KafkaListener(topics = Constants.Return_Goal_Money_Init,groupId = "Return_init1",errorHandler = "kafkaHandler")
    public void initiateTxnUser(String msg) throws ParseException, JsonProcessingException {
            JSONObject event = (JSONObject) jsonParser.parse(msg);
            String sender = "1111155555";
            String receiver = (String) event.get("username");
            CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
            createTransactionRequest.setReceiver(receiver);
            double amount = (double) event.get("amount");
            createTransactionRequest.setAmount(amount);
            createTransactionRequest.setPurpose("Transferring to User with interest");

            initiateTxn(sender,createTransactionRequest);

    }

    public String initiateTxn(String sender, CreateTransactionRequest createTransactionRequest) throws JsonProcessingException {

        Transaction transaction = createTransactionRequest.to(sender);

        txnRepository.save(transaction);

        kafkaTemplate.send(Constants.txnTopic,objectMapper.writeValueAsString(transaction));

        return transaction.getExternalTxnId();
    }

    @KafkaListener(topics = Constants.Wallet_Topic,groupId = "wallet123",errorHandler = "kafkaHandler")
    public void updateTxn(String msg) throws ParseException, JsonProcessingException, InterruptedException {
        JSONObject event = (JSONObject) jsonParser.parse(msg);

        String walletStatus = (String) event.get("walletUpdateStatus");
          String externalTxnId = (String) event.get("externalTxnId");
        logger.warn("info - {}",event);

          TransactionStatus transactionStatus;
          if(walletStatus.equals("Failed")){
              transactionStatus = TransactionStatus.FAILED;
          }else{
              transactionStatus = TransactionStatus.SUCCESSFUL;
          }
          txnRepository.updateTxn(externalTxnId,transactionStatus);

        Transaction transaction = txnRepository.findByExternalTxnId(externalTxnId);
        String details = objectMapper.writeValueAsString(transaction);

        if(transaction.getPurpose().equals("Transferring to Goal Deposit")){
            kafkaTemplate.send(Constants.Goal_User_Update,details);
        }
        else if(transaction.getPurpose().equals("Transferring to User with interest") && transaction.getTransactionStatus().equals(TransactionStatus.SUCCESSFUL)){
            kafkaTemplate.send(Constants.Final_Goal_Balance,transaction.getReceiver());
            Thread.sleep(2000);
            kafkaTemplate.send(Constants.Return_Money,details);
        }
        kafkaTemplate.send(Constants.Txn_Completed, details);

    }

    public List<Transaction> getAll(String username){
       return txnRepository.getAllTransaction(username);
    }


}
