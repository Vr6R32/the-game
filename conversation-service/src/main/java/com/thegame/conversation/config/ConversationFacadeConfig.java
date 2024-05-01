package com.thegame.conversation.config;

import com.thegame.clients.UserServiceClient;
import com.thegame.clients.WebSocketServiceClientProxy;
import com.thegame.conversation.conversation.*;
import com.thegame.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan("com.thegame.mapper")
public class ConversationFacadeConfig {

    private final ConversationMessageRepository conversationMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserServiceClient userServiceClient;
    private final WebSocketServiceClientProxy sessionClient;
    private final AuthMapper authMapper;

    @Bean
    public ConversationFacade conversationFacade() {
        ConversationService conversationService =
                new ConversationServiceImpl(conversationRepository,conversationMessageRepository,userServiceClient,sessionClient,authMapper);
        return new ConversationFacade(conversationService);
    }

}
