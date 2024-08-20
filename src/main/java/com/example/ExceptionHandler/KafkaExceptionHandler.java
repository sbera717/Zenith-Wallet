package com.example.ExceptionHandler;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Arrays;

@Configuration
public class KafkaExceptionHandler {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

//    @Bean
//    public DefaultErrorHandler errorHandler() {
//        return new DefaultErrorHandler(new FixedBackOff(2, 1)); // Retry immediately once
//    }
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
//            ConsumerFactory<String, String> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//
////        // Set to manual acknowledgment mode
////        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//
//        factory.setCommonErrorHandler(errorHandler());
//
//
//        return factory;
//    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        return new DefaultErrorHandler((consumerRecord, exception) -> {
            // Log the error or perform other error handling
            System.err.println("Error processing message: " + consumerRecord.value());
            System.err.println("topic" + consumerRecord.topic());
            exception.printStackTrace();

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                    "global-dead-letter-queue",
                    consumerRecord.topic() +" " + (String)consumerRecord.value() + " " + exception.getCause() // Value: Original message
            );

            // Add error details as a header
            producerRecord.headers().add(new RecordHeader("error", exception.getMessage().getBytes()));
           // kafkaTemplate.send(producerRecord);

            // Send the failed message to DLQ
            //kafkaTemplate.send("global-dead-letter-queue", consumerRecord.topic(), (String) consumerRecord.value());
        }, new FixedBackOff(50L, 10)); // Retry settings
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(defaultErrorHandler(kafkaTemplate));
        return factory;
    }
}