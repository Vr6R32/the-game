package com.thegame.websocket.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.ConversationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
@RequiredArgsConstructor
class NotificationServiceFacadeConfig {

    private final ConversationServiceClient conversationServiceClient;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Bean
    public NotificationFacade notificationFacade() {
        NotificationService notificationService = new NotificationServiceImpl(conversationServiceClient,objectMapper,messagingTemplate);
        return new NotificationFacade(notificationService);
    }
}
