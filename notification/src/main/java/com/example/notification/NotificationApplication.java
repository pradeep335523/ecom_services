package com.example.notification;

import com.example.notification.order.event.OrderNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@SpringBootApplication
@Slf4j
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @KafkaListener(topics = {"notificationTopic"})
    // TODO : Payload mapped to String and value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
    // due to deserialization error - Later it will be corrected.
    public void notificationListener(@Payload String event) {
        log.info("Received Event : {}",event);
        log.info("Notification Found --------------------Listening Event  Value : {}", event);

    }
}
