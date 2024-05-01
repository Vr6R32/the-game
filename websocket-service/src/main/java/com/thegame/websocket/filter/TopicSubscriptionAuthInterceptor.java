package com.thegame.websocket.filter;

import com.thegame.websocket.validator.SubscriptionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class TopicSubscriptionAuthInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String destination = headerAccessor.getDestination();
            WebsocketUserPrincipal user = (WebsocketUserPrincipal) headerAccessor.getUser();
            if(!SubscriptionValidator.validateSubscription(destination,user)){
                throw new MessagingException(String.format("UNAUTHORIZED TO SUBSCRIBE TOPIC -> [%S] BY USER -> [%s] ", destination, user.getName()));
            }
        }
        return message;
    }
}