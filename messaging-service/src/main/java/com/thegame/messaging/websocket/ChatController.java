package com.thegame.messaging.websocket;

import com.thegame.dto.AuthenticationUserObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {


    @MessageMapping("/sendMessage/{id}")
    @SendTo("/conversation/{id}")
    public ChatMessage sendMessage(@PathVariable String id, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        AuthenticationUserObject user = (AuthenticationUserObject) headerAccessor.getSessionAttributes().get("user");
        System.out.println(user);
        return new ChatMessage(user.username(), chatMessage.payload());
//        return chatMessage;
    }

}