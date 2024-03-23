package com.thegame.websocket.session;

import com.thegame.dto.UserSessionDTO;

public class UserSessionMapper {

    private UserSessionMapper() {
    }

    public static UserSessionDTO mapUserSessionToDTO(UserSession userSession) {
        return new UserSessionDTO(userSession.getUsername(), userSession.getUserId(), userSession.getStatus(), userSession.getLogoutTime());
    }
}
