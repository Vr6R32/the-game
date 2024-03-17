package com.thegame.messaging.websocket.session;

import com.thegame.messaging.websocket.filter.WebsocketUserPrincipal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSessionFacade {

    private final UserSessionService userSessionService;

    public void createSession(WebsocketUserPrincipal user){
        userSessionService.createSession(user);
    }
    public void deleteSession(WebsocketUserPrincipal user){
        userSessionService.deleteSession(user);
    }


}
