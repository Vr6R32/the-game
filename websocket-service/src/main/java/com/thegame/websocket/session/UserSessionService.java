package com.thegame.websocket.session;

import com.thegame.websocket.filter.WebsocketUserPrincipal;

interface UserSessionService {

    void createSession(WebsocketUserPrincipal principal);
    void deleteSession(WebsocketUserPrincipal principal);
    UserSession findUserSessionByUsername(String username);
}
