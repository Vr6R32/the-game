package com.thegame.websocket.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.ConversationServiceClient;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationFriendInfo;
import com.thegame.model.Status;
import com.thegame.websocket.filter.WebsocketUserPrincipal;
import com.thegame.websocket.utils.WebSocketPrincipalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.thegame.websocket.notification.NotificationType.FRIEND_SESSION_UPDATE;

@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {

    private final ConversationServiceClient conversationServiceClient;
    private final ObjectMapper objectMapper;

    @Lazy
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendUpdateSessionStatusEventToConversationFriends(WebsocketUserPrincipal user, Status status) {
        AuthenticationUserObject authenticationUserObject = WebSocketPrincipalMapper.mapWebSocketPrincipalToAuthUserObject(user);
        List<ConversationFriendInfo> allUserConversationSecondUserIds = conversationServiceClient.getAllUserConversationSecondUserIds(mapUserToJsonObject(authenticationUserObject));
        Date eventDate = Date.from(Instant.now().plus(Duration.ofHours(2)));
        for (ConversationFriendInfo conversationFriend : allUserConversationSecondUserIds) {
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(conversationFriend.secondUserId()),
                    "/notifications",
                    new Notification(FRIEND_SESSION_UPDATE, new SessionStatusUpdateEvent(conversationFriend.conversationId(), status, eventDate))
            );
        }

    }


    public String mapUserToJsonObject(AuthenticationUserObject appUser) {
        String jsonUserObject = null;
        try {
            jsonUserObject = objectMapper.writeValueAsString(appUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonUserObject;
    }

}