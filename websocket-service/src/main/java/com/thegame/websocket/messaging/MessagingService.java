package com.thegame.websocket.messaging;

import com.thegame.clients.ConversationServiceClient;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationDTO;
import com.thegame.dto.UserSessionDTO;
import com.thegame.mapper.AuthMapper;
import com.thegame.model.Notification;
import com.thegame.model.NotificationType;
import com.thegame.model.Status;
import com.thegame.request.ConversationMessageRequest;
import com.thegame.websocket.kafka.KafkaEvent;
import com.thegame.websocket.kafka.KafkaProducerService;
import com.thegame.websocket.session.UserSessionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.thegame.websocket.validator.ConversationAccessValidator.validateConversationAccess;


@Service
@RequiredArgsConstructor
class MessagingService {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserSessionFacade userSessionFacade;

    private final ConversationServiceClient conversationServiceClient;

    private final AuthMapper authMapper;

    private final KafkaProducerService kafkaProducerService;

    //TODO CREATE FACADE FOR THIS SERVICE


    public void handleReconnectingStatus(AuthenticationUserObject user) {
        userSessionFacade.setSessionStatusReconnect(user);
    }

    public void sendPrivateMessage(AuthenticationUserObject senderUser, UUID conversationId, ChatMessage chatMessage) {
        ConversationDTO conversation = conversationServiceClient
                .findConversationById(authMapper.mapUserToJsonObject(senderUser), conversationId);

        if(validateConversationAccess(conversation,senderUser)) {
            ConversationMessageRequest newMessageRequest = new ConversationMessageRequest(chatMessage.message());
            Date eventDate = conversationServiceClient.sendAndSaveNewConversationMessage(authMapper.mapUserToJsonObject(senderUser), conversationId, newMessageRequest);
            Long receiverId = senderUser.id().equals(conversation.firstUserId()) ? conversation.secondUserId() : conversation.firstUserId();

            ChatMessage validatedMsg = new ChatMessage(conversationId,senderUser.id(),chatMessage.message(), eventDate, receiverId);
            Notification notification = new Notification(NotificationType.CONVERSATION_MESSAGE, validatedMsg);

            messagingTemplate.convertAndSendToUser(String.valueOf(senderUser.id()), "/messages", notification);
            UserSessionDTO receiverSession = userSessionFacade.findUserSessionByUserId(receiverId);

            //TODO HANDLE CASE WHEN MONGO CONNECTION IS DEAD -< now it cant be dead because of scaling feature :X

            if(receiverSession != null && receiverSession.status().equals(Status.ONLINE)) {
                // TODO HANDLE CASE WHEN STATUS IS RECONNECTING
                kafkaProducerService.sendMessage(receiverSession.loggedInstanceId(), new KafkaEvent(String.valueOf(receiverId),"/messages", notification));
            }

        } else {
            throw new MessagingException("403 UNAUTHORIZED");
        }
    }
}
