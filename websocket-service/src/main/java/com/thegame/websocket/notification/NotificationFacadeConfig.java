package com.thegame.websocket.notification;

import com.thegame.clients.ConversationServiceClient;
import com.thegame.mapper.AuthMapper;
import com.thegame.websocket.kafka.KafkaProducerService;
import com.thegame.websocket.session.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan("com.thegame.mapper")
class NotificationFacadeConfig {

    private final ConversationServiceClient conversationServiceClient;
    private final AuthMapper authMapper;
    private final KafkaProducerService kafkaProducerService;
    private final UserSessionRepository userSessionRepository;

    @Bean
    public NotificationFacade notificationFacade() {
        NotificationService notificationService = new NotificationServiceImpl(conversationServiceClient,authMapper,kafkaProducerService,userSessionRepository);
        return new NotificationFacade(notificationService);
    }
}
