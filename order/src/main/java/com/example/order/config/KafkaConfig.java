package com.example.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static String NOTIFICATION_TOPIC="notificationTopic";
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(NOTIFICATION_TOPIC)
                .partitions(10)
                .replicas(1)
                .build();
    }

}
