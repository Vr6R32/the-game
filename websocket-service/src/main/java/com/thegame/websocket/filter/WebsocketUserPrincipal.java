package com.thegame.websocket.filter;

import java.security.Principal;

public record WebsocketUserPrincipal(String name, Long userId) implements Principal {


    @Override
    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }


}
