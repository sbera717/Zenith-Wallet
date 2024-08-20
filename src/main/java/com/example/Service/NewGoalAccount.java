package com.example.Service;

import com.example.Utils.Constants;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewGoalAccount {

    private final JSONParser jsonParser;
    private final SendEmailUser emailUser;
    private final KeyCloakCommunication keyCloakCommunication;

    @KafkaListener(topics = Constants.New_Goal_Account,groupId = "new_account_goal1",errorHandler = "kafkaHandler")
    public void newAccount(String msg) throws ParseException, MessagingException {
        JSONObject event = (JSONObject) jsonParser.parse(msg);
        String username = (String) event.get("username");
        String email = keyCloakCommunication.getEmail(username);
        String body = "Congratulations,\n" +
                "Your goal has been created, and soon you can check your balance with interest on the goal tab.\n\n" +
                "Keep saving!\n\n\n" +
                "Cheers,\n" +
                "Team Zenith";

        emailUser.sendEmail(email,body,"Goal Creation Done");

    }
}
