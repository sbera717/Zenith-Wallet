package com.example.Config;

import com.example.Utils.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Value(("${spring.kafka.bootstrap-servers}"))
    private String kafkaLogin;

    @Bean
    ProducerFactory<String,String> getProducerFactory(){
        Map<String,Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaLogin);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    KafkaTemplate<String,String> getKafkaTemplate(){
        return new KafkaTemplate<>(getProducerFactory());
    }

    @Bean
    ConsumerFactory<String,String> getConsumerFactory(){
        Map<String,Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaLogin);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        properties.put("auto.offset.reset", "earliest");

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        return new DefaultErrorHandler((consumerRecord, exception) -> {
            // Log the error or perform other error handling
            System.err.println("Error processing message: " + consumerRecord.value());
            exception.printStackTrace();

            // Send the failed message to DLQ
            kafkaTemplate.send("global-dead-letter-queue", consumerRecord.topic(), (String) consumerRecord.value());
        }, new FixedBackOff(1L, 2)); // Retry settings
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
        ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(defaultErrorHandler(getKafkaTemplate()));
        return factory;
    }



    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name(Constants.Wallet_Topic)
                .partitions(1)
                .replicas(1)
                .build();
    }


    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name(Constants.Return_Goal_Money_Init)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic3(){
        return TopicBuilder.name("global-dead-letter-queue")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic4(){
        return TopicBuilder.name(Constants.Final_Goal_Balance)
                .partitions(1)
                .replicas(1)
                .build();
    }






}
