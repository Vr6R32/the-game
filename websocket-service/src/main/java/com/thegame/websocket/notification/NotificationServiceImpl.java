package com.thegame.websocket.notification;

import com.thegame.clients.ConversationServiceClient;
import com.thegame.dto.ConversationFriendInfo;
import com.thegame.dto.UserSessionDTO;
import com.thegame.mapper.AuthMapper;
import com.thegame.model.Notification;
import com.thegame.model.Status;
import com.thegame.websocket.filter.WebsocketUserPrincipal;
import com.thegame.websocket.kafka.KafkaEvent;
import com.thegame.websocket.kafka.KafkaProducerService;
import com.thegame.websocket.session.UserSessionMapper;
import com.thegame.websocket.session.UserSessionRepository;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.thegame.model.NotificationType.FRIEND_SESSION_UPDATE;
import static com.thegame.websocket.utils.WebSocketPrincipalMapper.mapWebSocketPrincipalToAuthUserObject;

@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {

    public static final String NOTIFICATIONS_PATH = "/notifications";
    private final ConversationServiceClient conversationServiceClient;
    private final AuthMapper authMapper;
    private final KafkaProducerService kafkaProducerService;
    private final UserSessionRepository userSessionRepository;


    @Override
    public void sendUpdateSessionStatusEventToConversationFriends(WebsocketUserPrincipal user, Status status) {
        List<ConversationFriendInfo> allUserConversationSecondUserIds = conversationServiceClient.getAllUserConversationSecondUserIds(authMapper.mapUserToJsonObject(mapWebSocketPrincipalToAuthUserObject(user)));
        Date eventDate = Date.from(Instant.now().plus(Duration.ofHours(2)));

        for (ConversationFriendInfo conversationFriend : allUserConversationSecondUserIds) {
            //TODO FETCH USER SESSION IN ONE SELECT TRANSACTION
            UserSessionDTO userSession = userSessionRepository.findUserSessionByUserId(conversationFriend.secondUserId()).map(UserSessionMapper::mapUserSessionToDTO).orElse(null);
            kafkaProducerService.sendMessage(userSession.loggedInstanceId(),
                    new KafkaEvent(String.valueOf(conversationFriend.secondUserId()), NOTIFICATIONS_PATH, new Notification(FRIEND_SESSION_UPDATE, new SessionStatusUpdateEvent(conversationFriend.conversationId(), status, eventDate))));
        }
    }

    @Override
    public void sendConversationInvitationEventToSecondUser(Notification notification, Long secondUserId) {
        //TODO HANDLE CASE WHEN NOTIFICATION STATUS IS RECONNECTING
        // TEMPORARY SOLUTION (CIRCULAR DEPENDENCY) V
        UserSessionDTO userSession = userSessionRepository.findUserSessionByUserId(secondUserId).map(UserSessionMapper::mapUserSessionToDTO).orElse(null);
        kafkaProducerService.sendMessage(userSession.loggedInstanceId(),
                new KafkaEvent(String.valueOf(secondUserId), NOTIFICATIONS_PATH, new Notification(notification.type(), notification.payload())));
    }


    @Override
    public void sendConversationStatusUpdateEvent(Notification notification, Long secondUserId) {
        //TODO HANDLE CASE WHEN NOTIFICATION STATUS IS RECONNECTING
        // TEMPORARY SOLUTION (CIRCULAR DEPENDENCY) V
        UserSessionDTO userSession = userSessionRepository.findUserSessionByUserId(secondUserId).map(UserSessionMapper::mapUserSessionToDTO).orElse(null);
        kafkaProducerService.sendMessage(userSession.loggedInstanceId(),
                new KafkaEvent(String.valueOf(secondUserId), NOTIFICATIONS_PATH, new Notification(notification.type(), notification.payload())));
    }
}
