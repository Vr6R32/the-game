//package com.thegame.messaging;
//
//import org.springframework.context.ApplicationListener;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Component
//public class WebSocketSessionListener implements ApplicationListener<AbstractSubProtocolEvent> {
//
//    @Override
//    public void onApplicationEvent(AbstractSubProtocolEvent event) {
//        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
//        String username = StompHeaderAccessor.wrap(event.getMessage()).getUser().getName();
//
//        if (event instanceof SessionConnectedEvent) {
////            System.out.println(sessionId);
////            System.out.println(username);
//        } else if (event instanceof SessionDisconnectEvent) {
////            System.out.println(sessionId);
////            System.out.println(username);
//        }
//    }
//}