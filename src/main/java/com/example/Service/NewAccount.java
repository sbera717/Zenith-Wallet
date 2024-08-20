package com.example.Service;


import com.example.Utils.Constants;
import jakarta.mail.MessagingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NewAccount {


    @Autowired
    private SendEmailUser sendEmailUser;

    @Autowired
    JSONParser jsonParser;

    @Autowired
    private KeyCloakCommunication keyCloakCommunication;

    @KafkaListener(topics = Constants.New_Account,groupId = "new_account",errorHandler = "kafkaHandler")
    public void sendMsg(String email) throws ParseException, MessagingException {

        String body = "Congratulations " + ",\n" +
                    "Your account has been created and your wallet has been successfully activated.\n\n\n" +
                    "Cheers,\n" +
                    "Team Zenith";

            sendEmailUser.sendEmail(email,body,"New Account");


    }

}
