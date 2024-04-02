package com.thegame.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.ConversationServiceClient;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationDTO;
import com.thegame.dto.UserSessionDTO;
import com.thegame.request.ConversationMessageRequest;
import com.thegame.model.Status;
import com.thegame.websocket.session.UserSessionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static com.thegame.websocket.WebSocketManager.extractDestinationPathVariable;
import static com.thegame.websocket.WebSocketManager.extractUserFromSession;
import static com.thegame.websocket.validator.ConversationAccessValidator.validateConversationAccess;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserSessionFacade userSessionFacade;

    private final ConversationServiceClient conversationServiceClient;

    private final ObjectMapper objectMapper;


    @MessageMapping("/status/reconnect")
    public void handleReconnectingStatus(SimpMessageHeaderAccessor headerAccessor) {
        AuthenticationUserObject user = extractUserFromSession(headerAccessor);
        userSessionFacade.setSessionStatusReconnect(user);
    }

    @MessageMapping("/private/message/{id}")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage,SimpMessageHeaderAccessor headerAccessor) {
        AuthenticationUserObject senderUser = extractUserFromSession(headerAccessor);
        UUID conversationId = extractDestinationPathVariable(headerAccessor);

        ChatMessage validatedMsg = new ChatMessage(conversationId,senderUser.id(),chatMessage.payload());
        ConversationDTO conversation = conversationServiceClient
                .findConversationById(mapUserToJsonObject(senderUser), conversationId);

        if(validateConversationAccess(conversation,senderUser)) {
            ConversationMessageRequest newMessageRequest = new ConversationMessageRequest(chatMessage.payload());
            conversationServiceClient.sendAndSaveNewConversationMessage((mapUserToJsonObject(senderUser)),conversationId, newMessageRequest);


            messagingTemplate.convertAndSendToUser(String.valueOf(senderUser.id()), "/messages", validatedMsg);
            Long receiverId = senderUser.id().equals(conversation.firstUserId()) ? conversation.secondUserId() : conversation.firstUserId();

            UserSessionDTO receiverSession = userSessionFacade.findUserSessionByUserId(receiverId);
            if(receiverSession != null && receiverSession.status().equals(Status.ONLINE)) {
                messagingTemplate.convertAndSendToUser(String.valueOf(receiverId), "/messages", validatedMsg);
            }
        } else {
            throw new MessagingException("403 UNAUTHORIZED");
        }
    }

    private String mapUserToJsonObject(AuthenticationUserObject appUser) {
        String jsonUserObject = null;
        try {
            jsonUserObject = objectMapper.writeValueAsString(appUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonUserObject;
    }


}