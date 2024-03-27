package com.thegame.websocket.filter;

import com.thegame.model.Role;

import java.security.Principal;
import java.util.Date;


public record WebsocketUserPrincipal(String name, Long userId, Role role, String email, Date accessTokenExpiration) implements Principal {

    @Override
    public String getName() {
        return name;
    }

}
