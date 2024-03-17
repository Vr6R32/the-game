package com.thegame.messaging.websocket.filter;

import com.thegame.messaging.websocket.session.UserSessionFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketSessionListener implements ApplicationListener<AbstractSubProtocolEvent> {

    private final UserSessionFacade userSessionFacade;
    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {
        WebsocketUserPrincipal user = (WebsocketUserPrincipal) StompHeaderAccessor.wrap(event.getMessage()).getUser();
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();

        if (event instanceof SessionConnectedEvent) {
            userSessionFacade.createSession(user);
            log.info("SESSION -> [{}] CONNECTED BY USER -> [{}] WITH ID -> [{}]",sessionId,user.name(),user.userId());

        } else if (event instanceof SessionDisconnectEvent) {
            userSessionFacade.deleteSession(user);
            log.info("SESSION -> [{}] DISCONNECTED BY USER -> [{}] WITH ID -> [{}]",sessionId,user.name(),user.userId());

        }
    }
}