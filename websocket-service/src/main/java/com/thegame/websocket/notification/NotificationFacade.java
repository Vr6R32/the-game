package com.thegame.websocket.notification;

import com.thegame.model.Notification;
import com.thegame.model.Status;
import com.thegame.websocket.filter.WebsocketUserPrincipal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationService notificationService;

    public void sendUpdateSessionStatusEventToConversationFriends(WebsocketUserPrincipal user, Status status) {
        notificationService.sendUpdateSessionStatusEventToConversationFriends(user,status);
    }

    public void sendConversationInvitationEventToSecondUser(Notification notification, Long secondUserId) {
        notificationService.sendConversationInvitationEventToSecondUser(notification,secondUserId);
    }

    public void sendConversationStatusUpdateEvent(Notification notification, Long secondUserId) {
        notificationService.sendConversationStatusUpdateEvent(notification, secondUserId);
    }
}
