package com.thegame.clients;

import com.thegame.dto.UserSessionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class WebSocketSessionClientProxy {

    private final WebSocketSessionClient webSocketSessionClient;

    public WebSocketSessionClientProxy(WebSocketSessionClient webSocketSessionClient) {
        this.webSocketSessionClient = webSocketSessionClient;
    }

    public Map<UUID, UserSessionDTO> findConversationUserSessionsByIdMap(String user, Map<UUID, Long> conversationIdSecondUserIdMap) {
        try {
            return webSocketSessionClient.findConversationUserSessionsByIdMap(user, conversationIdSecondUserIdMap);
        } catch (Exception e) {
            log.info("Failed to retrieve user session details due to an internal server connection timeout error: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}