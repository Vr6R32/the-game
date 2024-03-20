package com.thegame.websocket.filter;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;


@Component
public class SubscriptionListener implements ApplicationListener<SessionSubscribeEvent> {


    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headerAccessor.getUser();
        WebsocketUserPrincipal user = (WebsocketUserPrincipal) principal;
        String username = user.getName();
        Long userId = user.getUserId();
        String sessionId = headerAccessor.getSessionId();
        String subscriptionId = headerAccessor.getSubscriptionId();
        String destination = headerAccessor.getDestination();
//        System.out.println(sessionId);
//        System.out.println(subscriptionId);
//        System.out.println(destination);



//        System.out.println(username);
//        System.out.println(userId);

    }
}