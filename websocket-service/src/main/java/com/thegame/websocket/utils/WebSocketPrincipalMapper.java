package com.thegame.websocket.utils;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.websocket.filter.WebsocketUserPrincipal;

public class WebSocketPrincipalMapper {

    private WebSocketPrincipalMapper() {
    }

    public static AuthenticationUserObject mapWebSocketPrincipalToAuthUserObject(WebsocketUserPrincipal principal) {
        return new AuthenticationUserObject(principal.userId(), principal.name(),principal.email(),principal.role(),principal.accessTokenExpiration());
    }
}
