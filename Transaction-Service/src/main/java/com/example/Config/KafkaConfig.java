package com.example.Config;


import com.example.Utils.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    public NewTopic topic1() {
        return TopicBuilder.name(Constants.txnTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name(Constants.Goal_User_Update)
                .partitions(1)
                .replicas(1)
                .build();
    }
}

