package com.thegame.websocket.messaging;

import com.thegame.dto.AuthenticationUserObject;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static com.thegame.websocket.messaging.WebSocketUserAuthHelper.extractDestinationPathVariable;
import static com.thegame.websocket.messaging.WebSocketUserAuthHelper.extractUserFromSession;

@Controller
@RequiredArgsConstructor
public class MessagingController {

    private final MessagingService messagingService;

    @MessageMapping("/status/reconnect")
    public void handleReconnectingStatus(SimpMessageHeaderAccessor headerAccessor) {
        AuthenticationUserObject user = extractUserFromSession(headerAccessor);
        messagingService.handleReconnectingStatus(user);
    }

    @MessageMapping("/private/message/{id}")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage,SimpMessageHeaderAccessor headerAccessor) {
        AuthenticationUserObject senderUser = extractUserFromSession(headerAccessor);
        UUID conversationId = extractDestinationPathVariable(headerAccessor);
        messagingService.sendPrivateMessage(senderUser, conversationId, chatMessage);
    }

}