package com.thegame.websocket.session;

import com.thegame.dto.UserSessionDTO;
import com.thegame.websocket.filter.WebsocketUserPrincipal;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class UserSessionFacade {

    private final UserSessionService userSessionService;

    public void setSessionStatusOnline(WebsocketUserPrincipal user){
        userSessionService.setSessionStatusOnline(user);
    }

    public void setSessionStatusOffline(WebsocketUserPrincipal user){
        userSessionService.setSessionStatusOffline(user);
    }
    public UserSessionDTO findUserSessionByUserId(Long userId) {
        return userSessionService.findUserSessionByUserId(userId);
    }

    public Map<UUID, UserSessionDTO> findUserSessionDetailsByIdsMap(Map<UUID, Long> conversationIdSecondUserIdMap) {
        return userSessionService.findUserSessionDetailsByIdsMap(conversationIdSecondUserIdMap);
    }

}
