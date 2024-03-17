package com.thegame.messaging.websocket.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Lazy
    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String destination = headerAccessor.getDestination();
            WebsocketUserPrincipal user = (WebsocketUserPrincipal) headerAccessor.getUser();
            if(user.getName().equals("karacz1")){
                throw new MessagingException(String.format("UNAUTHORIZED TO SUBSCRIBE TOPIC -> [%S] BY USER -> [%s] ", destination, user.getName()));
            }

        }
        return message;
    }

//    @Scheduled(fixedRate = 100)
//    public void testScheduler(){
//        messagingTemplate.convertAndSendToUser(
//                "1", "/errors", new ChatMessage("admin","message")
//        );
//    }

}