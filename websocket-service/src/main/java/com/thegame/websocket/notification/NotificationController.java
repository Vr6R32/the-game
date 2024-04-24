package com.thegame.websocket.notification;

import com.thegame.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/ws/notifications")
class NotificationController {

    private final NotificationFacade notificationFacade;

    @PostMapping("{secondUserId}")
    public void sendNewConversationNotificationEvent(@PathVariable("secondUserId") Long secondUserId, @RequestBody Notification notification) {
        notificationFacade.sendConversationInvitationEventToSecondUser(notification,secondUserId);
    }

    @PostMapping("conversation/status/update/{secondUserId}")
    public void sendConversationStatusUpdateEvent(@RequestBody Notification notification, @PathVariable("secondUserId") Long secondUserId) {
        notificationFacade.sendConversationStatusUpdateEvent(notification,secondUserId);
    }

}
