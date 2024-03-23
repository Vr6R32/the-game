package com.thegame.conversation.conversation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.UserServiceClient;
import com.thegame.clients.WebSocketSessionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConversationFacadeConfig {

    private final ConversationMessageRepository conversationMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserServiceClient userServiceClient;
    private final WebSocketSessionClient sessionClient;
    private final ObjectMapper objectMapper;

    @Bean
    public ConversationFacade conversationFacade() {
        ConversationService conversationService =
                new ConversationServiceImpl(conversationRepository,conversationMessageRepository,userServiceClient,sessionClient,objectMapper);
        return new ConversationFacade(conversationService);
    }

}
