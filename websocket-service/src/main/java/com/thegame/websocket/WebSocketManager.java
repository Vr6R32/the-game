package com.thegame.websocket;

import com.thegame.dto.AuthenticationUserObject;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Objects;

public class WebSocketManager {

    private WebSocketManager() {
    }

    public static AuthenticationUserObject extractUserFromSession(SimpMessageHeaderAccessor headerAccessor) {
        return (AuthenticationUserObject) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("user");
    }
}
