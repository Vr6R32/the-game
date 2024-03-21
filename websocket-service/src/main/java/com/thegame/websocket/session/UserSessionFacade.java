package com.thegame.websocket.session;

import com.thegame.websocket.filter.WebsocketUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class UserSessionFacade {

    private final UserSessionService userSessionService;

    @Async
    public void createSession(WebsocketUserPrincipal user){
        userSessionService.createSession(user);
    }
    @Async
    public void deleteSession(WebsocketUserPrincipal user){
        userSessionService.deleteSession(user);
    }

    public UserSession findActiveUserSessionByUserId(Long userId) {
        return userSessionService.findUserSessionByUsername(userId);
    }

}
