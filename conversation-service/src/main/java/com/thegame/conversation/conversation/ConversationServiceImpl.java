package com.thegame.conversation.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.UserServiceClient;
import com.thegame.conversation.entity.Conversation;
import com.thegame.conversation.entity.ConversationMessage;
import com.thegame.dto.*;
import com.thegame.request.ConversationMessageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ConversationServiceImpl(ConversationRepository conversationRepository,
                                      ConversationMessageRepository messageRepository,
                                      UserServiceClient userServiceClient,
                                      ObjectMapper objectMapper) implements ConversationService {


    @Override
    public List<DetailedConversationDTO> getAllUserConversations(AuthenticationUserObject user) {

        List<ConversationDTO> allConversationsByUserId = conversationRepository.findAllConversationsByUserId(user.id()).stream().map(ConversationMapper::mapConversationToDTO).toList();

        Map<UUID, Long> conversationIdSecondUserIdMap = new HashMap<>();

        for (ConversationDTO conversation : allConversationsByUserId) {
            if (!conversation.firstUserId().equals(user.id())) {
                conversationIdSecondUserIdMap.put(conversation.id(), conversation.firstUserId());
            } else if (!conversation.secondUserId().equals(user.id())) {
                conversationIdSecondUserIdMap.put(conversation.id(), conversation.secondUserId());
            }
        }

        Map<UUID, AppUserDTO> conversationsUsersDetails = userServiceClient.getConversationsUsersDetails(mapUserToJsonObject(user), conversationIdSecondUserIdMap);

        return conversationIdSecondUserIdMap.entrySet().stream()
                .map(entry -> {
                    UUID conversationId = entry.getKey();
                    Long secondUserId = entry.getValue();
                    AppUserDTO userDto = conversationsUsersDetails.get(conversationId);
                    String secondUserAvatarUrl = userDto.avatarUrl();
                    String secondUserEmail = userDto.email();
                    return new DetailedConversationDTO(conversationId, secondUserId, secondUserAvatarUrl, secondUserEmail);
                })
                .toList();
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


    @Override
    public ConversationDTO getConversationById(UUID uuid, AuthenticationUserObject user) {
        return conversationRepository.findConversationIdById(uuid)
                .map(ConversationMapper::mapConversationToDTO)
                .filter(dto -> user.id().equals(dto.firstUserId()) || user.id().equals(dto.secondUserId()))
                .orElseGet(() -> null);
    }

    @Override
    public List<ConversationMessageDTO> getAllConversationMessages(UUID conversationId, AuthenticationUserObject user) {
        return conversationRepository.findAllConversationMessagesByUserAndConversationId(conversationId, user.id())
                .stream()
                .map(ConversationMapper::mapConversationMessageToDTO)
                .toList();
    }

    @Override
    public String saveNewConversationMessage(UUID conversationId, AuthenticationUserObject user, ConversationMessageRequest request) {
        ConversationMessage newMessage = ConversationMessage.builder()
                .conversation(conversationRepository.getReferenceById(conversationId))
                .sender(user.username())
                .payload(request.payload())
                .build();

        ConversationMessage savedMessage = messageRepository.save(newMessage);
        return savedMessage.getId().toString();
    }

    @Override
    public Conversation createNewConversation(AuthenticationUserObject user, ConversationRequest request) {
        // USER SERVICE FIND BY EMAIL , IF NOT EXISTS SEND AN INVITATION
        Conversation newConversation = Conversation.builder()
                .firstUserId(user.id())
                .secondUserId(2L)
                .build();
        return conversationRepository.save(newConversation);
    }
}
