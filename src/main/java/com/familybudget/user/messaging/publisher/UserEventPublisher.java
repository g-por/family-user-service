package com.familybudget.user.messaging.publisher;

import com.familybudget.user.config.RabbitMQConfig;
import com.familybudget.user.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishUserCreatedEvent(UserCreatedEvent event) {
        log.info("Publishing user created event: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.USER_CREATED_ROUTING_KEY,
                event
        );
    }
}