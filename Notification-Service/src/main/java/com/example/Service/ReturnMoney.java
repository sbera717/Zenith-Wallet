package com.example.Service;


import com.example.Utils.Constants;
import jakarta.mail.MessagingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReturnMoney {
    private static Logger logger = LoggerFactory.getLogger(ReturnMoney.class);
    @Autowired
    private SendEmailUser emailUser;
    @Autowired
    private JSONParser jsonParser;

    @Autowired
    private KeyCloakCommunication keyCloakCommunication;

    @KafkaListener(topics = Constants.Return_Money,groupId = "return_money1",errorHandler = "kafkaHandler")
    public void returnMoneyToUser(String msg) throws ParseException, MessagingException {
        logger.warn("{}",msg);
        JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
        String username = (String) jsonObject.get("receiver");
        String email =
                keyCloakCommunication.getEmail(username);
        String body = "Congratulations,\n" +
                "We are happy to inform you that your wallet has been credited with the goal money you have saved with us, along with the accumulated interest.\n\n\n" +
                "Thank you for your trust and commitment to saving with us. We look forward to helping you achieve more of your financial goals in the future.\n\n\n" +
                "Best regards,\n" +
                "Team Zenith";

        emailUser.sendEmail(email,body,"Goal Money credited to account");

    }


}
