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
public class TransactionRecord {

    private  static Logger logger = LoggerFactory.getLogger(TransactionRecord.class);
    @Autowired
    private JSONParser jsonParser;

    @Autowired
    private SendEmailUser emailUser;

    @Autowired
    private KeyCloakCommunication keyCloakCommunication;

    @KafkaListener(topics = Constants.Txn_Completed,groupId = "txn_123",errorHandler = "kafkaHandler")
    public void sendNoti(String msg) throws ParseException, MessagingException {
        logger.warn("{}",msg);
        JSONObject event = (JSONObject) jsonParser.parse(msg);
            String sender = (String) event.get("sender"); // valueof if you not sure that string can return null values
            String receiver = String.valueOf(event.get("receiver"));
            String senderEmail =
                    keyCloakCommunication.getEmail(sender);
            String receiverEmail =
                keyCloakCommunication.getEmail(receiver);
            double amount = (double) event.get("amount");
            String purpose = (String) event.get("purpose");
            String externalTxnId = (String) event.get("externalTxnId");
            String transactionStatus = (String) event.get("transactionStatus");

        if(!transactionStatus.equals("Failed")){
            String receiverMsg = "Hi User,\n" +
                    "Your account has been credited with the amount of " + amount + " for the transaction done by " + sender + " for the purpose of " + purpose + ".\n\n\n" +
                    "Cheers,\n" +
                    "Team Zenith";
            emailUser.sendEmail(receiverEmail,receiverMsg,Constants.EMAIL_SUBJECT);
        }

        String senderMsg = "Hi User,\n" +
                "Your transaction with ID " + externalTxnId + " of amount " + amount + " has been " + transactionStatus + ".\n\n\n" +
                "Cheers,\n" +
                "Team Zenith";

        emailUser.sendEmail(senderEmail,senderMsg,Constants.EMAIL_SUBJECT);

    }
}


