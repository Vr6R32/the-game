package com.thegame.conversation.conversation;

import com.thegame.conversation.entity.Conversation;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationDTO;
import com.thegame.dto.ConversationMessageDTO;
import com.thegame.dto.DetailedConversationDTO;
import com.thegame.request.ConversationMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class ConversationFacade {

    private final ConversationService conversationService;

    public List<DetailedConversationDTO> getAllUserConversations(AuthenticationUserObject user) {
        return conversationService.getAllUserConversations(user);
    }

    public ConversationDTO getConversationById(UUID uuid, AuthenticationUserObject user) {
        return conversationService.getConversationById(uuid,user);
    }

    @Transactional
    public List<ConversationMessageDTO> getAllConversationMessages(UUID conversationId, AuthenticationUserObject user) {
        return conversationService.getAllConversationMessages(conversationId, user);
    }
    @Transactional
    public String saveNewConversationMessage(UUID conversationId, AuthenticationUserObject user, ConversationMessageRequest request) {
        return conversationService.saveNewConversationMessage(conversationId, user, request);
    }

    public Conversation createNewConversation(AuthenticationUserObject user, ConversationRequest request) {
        return conversationService.createNewConversation(user, request);
    }
}
