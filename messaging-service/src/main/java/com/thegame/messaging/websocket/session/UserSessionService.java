package com.thegame.messaging.websocket.session;

import com.thegame.messaging.websocket.filter.WebsocketUserPrincipal;

interface UserSessionService {

    void createSession(WebsocketUserPrincipal principal);
    void deleteSession(WebsocketUserPrincipal principal);
}
