package com.familybudget.user.messaging.consumer;

import com.familybudget.user.config.RabbitMQConfig;
import com.familybudget.user.dto.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventListener {

    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Received user created event: {}", event);

        try {
            log.info("Processing user created event for: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error processing user created event: {}", e.getMessage(), e);
            throw e;
        }
    }
}