package com.thegame.conversation.conversation;

import com.thegame.conversation.entity.Conversation;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationDTO;
import com.thegame.dto.ConversationMessageDTO;
import com.thegame.dto.DetailedConversationDTO;
import com.thegame.request.ConversationMessageRequest;


import java.util.List;
import java.util.UUID;

record ConversationFacade(ConversationService conversationService) {

    public List<DetailedConversationDTO> getAllUserConversations(AuthenticationUserObject user) {
        return conversationService.getAllUserConversations(user);
    }

    public ConversationDTO getConversationById(UUID uuid, AuthenticationUserObject user) {
        return conversationService.getConversationById(uuid,user);
    }

    public List<ConversationMessageDTO> getAllConversationMessages(UUID conversationId, AuthenticationUserObject user) {
        return conversationService.getAllConversationMessages(conversationId, user);
    }

    public String saveNewConversationMessage(UUID conversationId, AuthenticationUserObject user, ConversationMessageRequest request) {
        return conversationService.saveNewConversationMessage(conversationId, user, request);
    }

    public Conversation createNewConversation(AuthenticationUserObject user, ConversationRequest request) {
        return conversationService.createNewConversation(user, request);
    }
}
