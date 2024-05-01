package com.thegame.websocket.messaging;

import com.thegame.dto.AuthenticationUserObject;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Objects;
import java.util.UUID;

public class WebSocketUserAuthHelper {

    private WebSocketUserAuthHelper() {
    }

    public static AuthenticationUserObject extractUserFromSession(SimpMessageHeaderAccessor headerAccessor) {
        return (AuthenticationUserObject) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("user");
    }
    public static UUID extractDestinationPathVariable(SimpMessageHeaderAccessor headerAccessor) {
        String destination = headerAccessor.getDestination();
        return UUID.fromString(destination.substring(destination.lastIndexOf('/') + 1));
    }
}
