package com.thegame.websocket.notification;

import com.thegame.model.Status;
import com.thegame.websocket.filter.WebsocketUserPrincipal;

interface NotificationService {

    void sendUpdateSessionStatusEventToConversationFriends(WebsocketUserPrincipal user, Status status);
}
