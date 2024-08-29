package com.example.Service;

import com.example.Utils.Constants;
import jakarta.mail.MessagingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class InterestNotification {

    @Autowired
    private SendEmailUser emailUser;

    @Autowired
    private JSONParser jsonParser;

    @Autowired
    private KeyCloakCommunication keyCloakCommunication;

    @KafkaListener(topics = Constants.Interest_Done,groupId = "interest1",errorHandler = "kafkaHandler")
    public void interestDone(String msg) throws ParseException, MessagingException {
        JSONObject event = (JSONObject) jsonParser.parse(String.valueOf(msg));
        String username = (String) event.get("username");
        String email = keyCloakCommunication.getEmail(username);
        String status = (String) event.get("goalTxnStatus");
        System.out.println("new account" + email + "status" + status) ;
        if (!status.equals("FAILED")) {
            String body = "Hi User,\n" +
                    "Your money has been successfully debited from your wallet towards your goal.\n\n\n" +
                    "Cheers,\n" +
                    "Team Zenith";

            emailUser.sendEmail(email,body,"Interest Processed Successfully");

        }else{
            String body = "Hi User,\n" +
                    "We regret to inform you that we were unable to debit money from your wallet towards your goal. " +
                    "We will try again in a few days. If you have any concerns, please contact our support team.\n\n\n" +
                    "Thank you for your understanding.\n" +
                    "Cheers,\n" +
                    "Team Zenith";
            emailUser.sendEmail(email,body,"Interest Processed Failed");
        }



    }
}
