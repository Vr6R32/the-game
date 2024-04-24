package com.thegame.websocket.notification;

import com.thegame.model.Notification;
import com.thegame.model.Status;
import com.thegame.websocket.filter.WebsocketUserPrincipal;

interface NotificationService {

    void sendUpdateSessionStatusEventToConversationFriends(WebsocketUserPrincipal user, Status status);

    void sendConversationInvitationEventToSecondUser(Notification notification, Long secondUserId);

    void sendConversationStatusUpdateEvent(Notification notification, Long secondUserId);

}
