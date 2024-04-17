package com.thegame.conversation.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.UserServiceClient;
import com.thegame.clients.WebSocketSessionClientProxy;
import com.thegame.conversation.entity.Conversation;
import com.thegame.conversation.entity.ConversationMessage;
import com.thegame.model.ConversationStatus;
import com.thegame.dto.*;
import com.thegame.model.Notification;
import com.thegame.model.NotificationType;
import com.thegame.model.Status;
import com.thegame.request.ConversationMessageRequest;
import com.thegame.request.NewConversationRequest;
import com.thegame.response.NewConversationResponse;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.*;

public record ConversationServiceImpl(ConversationRepository conversationRepository,
                                      ConversationMessageRepository messageRepository,
                                      UserServiceClient userServiceClient,
                                      WebSocketSessionClientProxy webSocketSessionClient,
                                      ObjectMapper objectMapper) implements ConversationService {


    @Override
    public List<DetailedConversationDTO> getAllUserConversations(AuthenticationUserObject user) {

        Map<UUID, ConversationInfo> conversationInfoMap = getConversationUserIdMap(user);

        Map<UUID, Long> conversationIdSecondUserIdMap = new HashMap<>();
        conversationInfoMap.forEach((conversationId, conversationInfo) -> conversationIdSecondUserIdMap.put(conversationId, conversationInfo.secondUserId()));

        Map<UUID, AppUserDTO> conversationsUsersDetails = userServiceClient.getConversationsUsersDetails(mapUserToJsonObject(user), conversationIdSecondUserIdMap);
        Map<UUID, UserSessionDTO> conversationUsersSessionDetails = webSocketSessionClient.findConversationUserSessionsByIdMap(mapUserToJsonObject(user), conversationIdSecondUserIdMap);

        return conversationInfoMap.entrySet().stream().map(entry -> {
            UUID conversationId = entry.getKey();
            ConversationInfo conversationInfo = entry.getValue();

            AppUserDTO userDto = conversationsUsersDetails.get(conversationId);
            UserSessionDTO userSessionDTO = conversationUsersSessionDetails.get(conversationId);

            return new DetailedConversationDTO(
                    conversationId,
                    conversationInfo.secondUserId(),
                    userDto != null ? userDto.avatarUrl() : null,
                    userDto != null ? userDto.email() : null,
                    userSessionDTO != null ? userSessionDTO.status() : null,
                    userSessionDTO != null ? userSessionDTO.logoutTime() : null,
                    conversationInfo.lastMessageDate(),
                    conversationInfo.secondUserContactName(),
                    conversationInfo.status(),
                    conversationInfo.isUnread()
            );
        }).toList();
    }

    @Override
    public List<ConversationFriendInfo> getAllUserConversationSecondUserIds(AuthenticationUserObject user) {
        return conversationRepository.findAllOtherUserIdsInConversations(user.id());
    }


    @Override
    public ConversationDTO getConversationById(UUID uuid, AuthenticationUserObject user) {
        return conversationRepository.findConversationIdById(uuid)
                .map(ConversationMapper::mapConversationToDTO)
                .filter(dto -> user.id().equals(dto.firstUserId()) || user.id().equals(dto.secondUserId()))
                .orElseGet(() -> null);
    }

    @Override
    public List<ConversationMessageDTO> getAllConversationMessages(UUID conversationId, AuthenticationUserObject user) {
        List<ConversationMessageDTO> messageList = conversationRepository.findAllConversationMessagesByUserAndConversationId(conversationId, user.id())
                .stream()
                .map(ConversationMapper::mapConversationMessageToDTO)
                .toList();

        conversationRepository.updateMessagesReadByReceiverIfNecessary(conversationId,user.id());

        return messageList;
    }

    @Override
    public String saveNewConversationMessage(UUID conversationId, AuthenticationUserObject user, ConversationMessageRequest request) {

        Date messageSendDate = Date.from(Instant.now());

        conversationRepository.updateLastMessageInfo(conversationId, messageSendDate, user.id() ,false);

        ConversationMessage newMessage = ConversationMessage.builder()
                .conversation(conversationRepository.getReferenceById(conversationId))
                .senderId(user.id())
                .payload(request.payload())
                .messageSendDate(messageSendDate)
                .build();

        ConversationMessage savedMessage = messageRepository.save(newMessage);
        return savedMessage.getId().toString();
    }

    @Override
    public NewConversationResponse createNewConversation(AuthenticationUserObject user, NewConversationRequest request) {

        // TODO USER SERVICE FIND BY EMAIL , IF NOT EXISTS SEND AN EMAIL INVITATION

        AppUserDTO secondUser = userServiceClient.getUserDetailsByEmailAddress(mapUserToJsonObject(user), request.secondUserEmail());

        Long secondUserId = secondUser.id();

        if(secondUserId==null) { secondUserId = userServiceClient.createInvitedUserAccount(mapUserToJsonObject(user), request); }

        if(conversationRepository.findConversationByFirstUserIdAndSecondUserId(user.id(), secondUserId).isPresent()) return new NewConversationResponse("CONVERSATION ALREADY EXIST", HttpStatus.OK,null);

        Conversation newConversation = Conversation.builder()
                .firstUserId(user.id())
                .secondUserId(secondUserId)
                .status(ConversationStatus.INVITATION)
                .secondUserContactName(request.secondUserContactName())
                .statusUpdatedByUserId(user.id())
                .build();

        conversationRepository.save(newConversation);

        // TODO IF USER EXISTS CHECK STATUS IF LOGGED IN , SEND NOTIFICATION

        UserSessionDTO secondUserSessionDetails = webSocketSessionClient.findUserSessionDetailsById(mapUserToJsonObject(user), secondUserId);

        Status userStatus = null;
        Date logoutTime = null;

        if (secondUserSessionDetails != null) {
            userStatus = secondUserSessionDetails.status();
            logoutTime = secondUserSessionDetails.logoutTime();
        }

        if (userStatus == Status.ONLINE || userStatus == Status.RECONNECTING) {
            AppUserDTO initiator = userServiceClient.getUserDetailsByEmailAddress(mapUserToJsonObject(user), user.email());

            DetailedConversationDTO secondUserNotification = new DetailedConversationDTO(newConversation.getId(),user.id(),initiator.avatarUrl(),initiator.email(),
                    Status.ONLINE,null,null,user.username(),ConversationStatus.INVITATION,false);

            webSocketSessionClient.sendNewConversationNotificationEvent(mapUserToJsonObject(user), new Notification(NotificationType.CONVERSATION_INVITATION, secondUserNotification),secondUserId);

        }

        //TODO FETCH DATA, LIKE USER STATUS , LOGOUT DATE AND AVATAR

        return new NewConversationResponse("CONVERSATION CREATED", HttpStatus.OK, new DetailedConversationDTO(
                newConversation.getId(),
                secondUserId,
                secondUser.avatarUrl(),
                request.secondUserEmail(),
                userStatus,
                logoutTime,
                null,
                newConversation.getSecondUserContactName(),
                ConversationStatus.INVITATION,
                false
        ));
    }




    private Map<UUID, ConversationInfo> getConversationUserIdMap(AuthenticationUserObject user) {

        List<ConversationDTO> allConversationsByUserId =
                conversationRepository.findAllConversationsByUserId(user.id()).stream().map(ConversationMapper::mapConversationToDTO).toList();

        Map<UUID, ConversationInfo> conversationInfoMap = new LinkedHashMap<>();
        for (ConversationDTO conversation : allConversationsByUserId) {
            Long secondUserId = conversation.firstUserId().equals(user.id()) ? conversation.secondUserId() : conversation.firstUserId();
            boolean isUnread = conversation.lastMessageSenderId() != null && conversation.lastMessageSenderId().equals(secondUserId) && !conversation.isReadByReceiver();
            conversationInfoMap.put(conversation.id(), new ConversationInfo(secondUserId, conversation.lastMessageDate(),
                    conversation.secondUserContactName(),conversation.status(),isUnread));
        }

        return conversationInfoMap;
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
