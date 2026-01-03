package com.practicum.user_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String ACCOUNT_TOPIC = "identity.accounts";
    public static final String LEARNING_TOPIC = "learning.events";

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name(ACCOUNT_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic learningTopic() {
        return TopicBuilder.name(LEARNING_TOPIC).partitions(3).replicas(1).build();
    }
}
