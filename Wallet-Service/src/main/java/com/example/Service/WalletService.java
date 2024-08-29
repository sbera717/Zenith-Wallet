package com.example.Service;

import com.example.Models.Wallet;
import com.example.Utils.Constants;
import com.example.Repository.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class WalletService{
    @Autowired
    WalletRepository walletRepository;
    private static Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JSONParser jsonParser;

    @Value("${wallet.balance}")
    private Double openingBalance;





    @KafkaListener(topics = {Constants.User_Topic},groupId = "wallet_1",errorHandler = "kafkaHandler")
    public void create(String msg) throws ParseException, JsonProcessingException {
        System.out.println(msg);
        JSONObject event = (JSONObject) jsonParser.parse(msg);
        String mobile = (String) event.get("mobile");
        if(mobile  == null){
            logger.warn("create: unable to find mobile in the event, data = {}",event);
            return;
        }



        Wallet wallet = Wallet.builder()
                .mobile(mobile)
                .balance(openingBalance)
                .walletStatus(true)
                .build();

        JSONObject newAccount = objectMapper.convertValue(wallet, JSONObject.class);
        kafkaTemplate.send(Constants.New_Account,objectMapper.writeValueAsString(newAccount));

        walletRepository.save(wallet);
    }

    @KafkaListener(topics = {Constants.Txn_Topic},groupId = "walletTxn_1",errorHandler = "kafkaHandler")
    public void update(String msg) throws ParseException, JsonProcessingException {
        JSONObject event = (JSONObject) jsonParser.parse(msg);
        String sender = (String) event.get("sender");
        String receiver = (String) event.get("receiver");
        Double amount = (double) event.get("amount");
        logger.warn("print - {} s  {} s  {}",sender,receiver,amount);
        String externalTxnId = (String) event.get("externalTxnId");

        Wallet senderTran = walletRepository.findByMobile(sender);
        Wallet receiverTran = walletRepository.findByMobile(receiver);

        JSONObject message = new JSONObject();
        message.put("receiver",receiver);
        message.put("sender",sender);
        message.put("amount",amount);
        message.put("externalTxnId",externalTxnId);

        if(receiverTran == null){
            logger.warn("Receiver number is invalid");
            message.put("walletUpdateStatus","Failed");
            kafkaTemplate.send(Constants.Wallet_Topic, objectMapper.writeValueAsString(message));
            return;
        } else if (senderTran.getBalance() < amount) {
            logger.warn("Insufficient Balance");
            message.put("walletUpdateStatus","Failed");
            kafkaTemplate.send(Constants.Wallet_Topic, objectMapper.writeValueAsString(message));
            return;
        }
        try {
            walletRepository.updateWallet(sender, -amount);
            walletRepository.updateWallet(receiver, amount);
            message.put("walletUpdateStatus","Success");
            kafkaTemplate.send(Constants.Wallet_Topic,objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            message.put("walletUpdateStatus","Failed");
            kafkaTemplate.send(Constants.Wallet_Topic, objectMapper.writeValueAsString(message));
        }


    }
    public Double getBalance(String username){
        Wallet wallet = walletRepository.findByMobile(username);
        return wallet.getBalance();
    }

    public Double addMoney(String username,Double money){
        Wallet wallet = walletRepository.findByMobile(username);
        Double moneyAdd = wallet.getBalance() + money;
        wallet.setBalance(moneyAdd);
        walletRepository.save(wallet);
        return moneyAdd;

    }

    //@KafkaListener(topics = Constants.User_Delete_Account,groupId = "user_delete1",errorHandler = "kafkaHandler")
    public void walletDelete(String mobile){
        try{
            Wallet wallet = walletRepository.findByMobile(mobile);
            walletRepository.deleteById(wallet.getWalletId());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
