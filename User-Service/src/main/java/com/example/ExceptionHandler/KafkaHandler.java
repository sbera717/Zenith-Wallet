package com.example.ExceptionHandler;//package com.example.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@Component
public class KafkaHandler implements ConsumerAwareListenerErrorHandler {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {

        JSONObject jsonObject = new JSONObject();
        Map<String,Object> store = message.getHeaders();
        String msg1 = String.valueOf(exception.getCause());
        jsonObject.put("topic",store.get("kafka_receivedTopic"));
        jsonObject.put("data",message.getPayload());
        jsonObject.put("Error Cause",msg1);
        String msg;
        try {
            msg  = objectMapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send("global-dead-letter-queue",msg);
        return new FixedBackOff(50, 10);
    }
}
