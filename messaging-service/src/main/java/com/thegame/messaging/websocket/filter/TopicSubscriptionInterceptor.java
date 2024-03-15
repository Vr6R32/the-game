package com.thegame.messaging.websocket.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class TopicSubscriptionInterceptor implements ChannelInterceptor {


    private final ObjectMapper objectMapper;


    @Lazy
    private final SimpMessagingTemplate messagingTemplate;


    private static Logger logger = org.slf4j.LoggerFactory.getLogger(TopicSubscriptionInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String destination = headerAccessor.getDestination();
            String username = headerAccessor.getUser().getName();
            if(username.equals("karacz1")){
                messagingTemplate.convertAndSendToUser(
                        username, "/errors", "403 FORBIDDEN"
                );
                throw new MessagingException(String.format("UNAUTHORIZED TO SUBSCRIBE TOPIC -> [%S] BY USER -> [%s] ", destination, username));
            }

        }
        return message;
    }

}