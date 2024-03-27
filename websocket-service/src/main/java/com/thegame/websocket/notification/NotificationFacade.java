package com.thegame.websocket.notification;

import com.thegame.model.Status;
import com.thegame.websocket.filter.WebsocketUserPrincipal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationService notificationService;

    public void sendUpdateSessionStatusEventToConversationFriends(WebsocketUserPrincipal user, Status status) {
        notificationService.sendUpdateSessionStatusEventToConversationFriends(user,status);
    }
}
