package com.thegame.websocket.session;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.UserSessionDTO;
import com.thegame.websocket.filter.WebsocketUserPrincipal;

import java.util.Map;
import java.util.UUID;

interface UserSessionService {

    void setSessionStatusOnline(WebsocketUserPrincipal principal);

    void setSessionStatusOffline(WebsocketUserPrincipal principal);

    UserSessionDTO findUserSessionByUserId(Long userId);

    Map<UUID, UserSessionDTO> findUserSessionDetailsByIdsMap(Map<UUID, Long> conversationIdSecondUserIdMap);

    void setSessionStatusReconnect(AuthenticationUserObject user);

}
