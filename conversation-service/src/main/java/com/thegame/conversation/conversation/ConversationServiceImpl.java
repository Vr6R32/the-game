package com.thegame.conversation.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.UserServiceClient;
import com.thegame.clients.WebSocketSessionClientProxy;
import com.thegame.conversation.entity.Conversation;
import com.thegame.conversation.entity.ConversationMessage;
import com.thegame.model.ConversationStatus;
import com.thegame.dto.*;
import com.thegame.request.ConversationMessageRequest;

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
    public Conversation createNewConversation(AuthenticationUserObject user, ConversationRequest request) {
        // TODO USER SERVICE FIND BY EMAIL , IF NOT EXISTS SEND AN EMAIL INVITATION
        Long secondUserId = userServiceClient.getUserIdByEmailAddress(mapUserToJsonObject(user), request.secondUserEmail());

        Conversation newConversation = Conversation.builder()
                .firstUserId(user.id())
                .secondUserId(secondUserId)
                .status(ConversationStatus.INVITATION)
                .secondUserContactName(request.secondUserContactName())
                .statusUpdatedByUserId(user.id())
                .build();

        return conversationRepository.save(newConversation);
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
