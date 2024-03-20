package com.thegame.websocket;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.websocket.session.Status;
import com.thegame.websocket.session.UserSession;
import com.thegame.websocket.session.UserSessionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import static com.thegame.websocket.WebSocketManager.extractUserFromSession;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserSessionFacade userSessionFacade;

//    @MessageMapping("/conversation/{id}")
//    public ChatMessage sendMessage(@PathVariable String id, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        AuthenticationUserObject user = extractUserFromSession(headerAccessor);
////        return new ChatMessage(user.username(), chatMessage.payload());
//        System.out.println(chatMessage);
//        return chatMessage;
//    }
//
//    @MessageMapping("/user/{username}/messages")
//    @SendTo("/user/{username}/messages")
//    public ChatMessage sendPrivateMessage(@PathVariable String username, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        AuthenticationUserObject userSender = extractUserFromSession(headerAccessor);
//        return new ChatMessage(userSender.username(), chatMessage.payload());
//    }


    @MessageMapping("/sendMessage/{id}")
    public void sendMessage(@Payload ChatMessage chatMessage, @PathVariable String id) {
        messagingTemplate.convertAndSend("/conversation/" + id, chatMessage);
    }

    @MessageMapping("/private/message")
    public ChatMessage sendPrivateMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        AuthenticationUserObject senderUser = extractUserFromSession(headerAccessor);
        String senderUsername = senderUser.username();
        ChatMessage validatedMsg = new ChatMessage(senderUsername, chatMessage.receiver(), chatMessage.payload());



        messagingTemplate.convertAndSendToUser(senderUsername, "/messages", validatedMsg);

        UserSession receiverSession = userSessionFacade.findUserSessionByUsername(chatMessage.receiver());
        if(receiverSession!=null && receiverSession.getStatus().equals(Status.ONLINE)) {
            messagingTemplate.convertAndSendToUser(chatMessage.receiver(), "/messages", validatedMsg);
        }
        return chatMessage;
    }


}