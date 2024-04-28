package com.thegame.conversation.conversation;

import com.thegame.dto.*;
import com.thegame.request.ConversationMessageRequest;
import com.thegame.request.ConversationStatusUpdateRequest;
import com.thegame.request.NewConversationRequest;
import com.thegame.response.ConversationStatusUpdateResponse;
import com.thegame.response.NewConversationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ConversationFacade {

    private final ConversationService conversationService;

    public List<DetailedConversationDTO> getAllUserConversations(AuthenticationUserObject user) {
        return conversationService.getAllUserConversations(user);
    }

    public List<ConversationFriendInfo> getAllUserConversationSecondUserIds(AuthenticationUserObject user) {
        return conversationService.getAllUserConversationSecondUserIds(user);
    }

    public ConversationDTO getConversationById(UUID uuid, AuthenticationUserObject user) {
        return conversationService.getConversationById(uuid,user);
    }

    @Transactional
    public Page<ConversationMessageDTO> getAllConversationMessages(UUID conversationId, AuthenticationUserObject user, int pageSize, int pageNumber) {
        return conversationService.getAllConversationMessages(conversationId, user, pageSize, pageNumber);
    }
    @Transactional
    public Date saveNewConversationMessage(UUID conversationId, AuthenticationUserObject user, ConversationMessageRequest request) {
        return conversationService.saveNewConversationMessage(conversationId, user, request);
    }

    public NewConversationResponse createNewConversation(AuthenticationUserObject user, NewConversationRequest request) {
        return conversationService.createNewConversation(user, request);
    }

    public ConversationStatusUpdateResponse updateConversationStatus(AuthenticationUserObject user, ConversationStatusUpdateRequest request) {
        return conversationService.updateConversationStatus(user, request);
    }
}
