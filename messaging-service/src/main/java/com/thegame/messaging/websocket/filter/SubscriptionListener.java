package com.thegame.messaging.websocket.filter;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;


@Component
public class SubscriptionListener implements ApplicationListener<SessionSubscribeEvent> {


    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = headerAccessor.getUser().getName();
//        String sessionId = headerAccessor.getSessionId();
//        String subscriptionId = headerAccessor.getSubscriptionId();
//        String destination = headerAccessor.getDestination();
//        System.out.println(username);
//        System.out.println(sessionId);
//        System.out.println(subscriptionId);
//        System.out.println(destination);

    }
}