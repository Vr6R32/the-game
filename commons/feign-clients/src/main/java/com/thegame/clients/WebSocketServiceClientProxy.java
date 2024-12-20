package com.thegame.clients;

import com.thegame.dto.UserSessionDTO;
import com.thegame.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class WebSocketServiceClientProxy {

    public static final String CONNECTION_TIMEOUT_EXCEPTION = "Failed to retrieve user session details due to an internal server connection timeout error: {}";
    private final WebSocketServiceClient webSocketServiceClient;

    public WebSocketServiceClientProxy(WebSocketServiceClient webSocketServiceClient) {
        this.webSocketServiceClient = webSocketServiceClient;
    }

    public Map<UUID, UserSessionDTO> findConversationUserSessionsByIdMap(String user, Map<UUID, Long> conversationIdSecondUserIdMap) {
        try {
            return webSocketServiceClient.findConversationUserSessionsByIdMap(user, conversationIdSecondUserIdMap);
        } catch (Exception e) {
            log.info(CONNECTION_TIMEOUT_EXCEPTION, e.getMessage());
            return Collections.emptyMap();
        }
    }

    public UserSessionDTO findUserSessionDetailsById(String user, Long userId) {
        try {
            return webSocketServiceClient.findUserSessionDetailsById(user,userId);
        } catch (Exception e) {
            log.info(CONNECTION_TIMEOUT_EXCEPTION, e.getMessage());
            return null;
        }
    }

    public void sendNewConversationNotificationEvent(String user, Notification notification,Long secondUserId) {
        try {
            webSocketServiceClient.sendNewConversationNotificationEvent(user,notification,secondUserId);
        } catch (Exception e) {
            log.info(CONNECTION_TIMEOUT_EXCEPTION, e.getMessage());
        }
    }

    public void sendConversationStatusUpdateEvent(String user, Notification notification, Long secondUserId) {
        try {
            webSocketServiceClient.sendConversationStatusUpdateEvent(user,notification,secondUserId);
        } catch (Exception e) {
            log.info(CONNECTION_TIMEOUT_EXCEPTION, e.getMessage());
        }
    }
}