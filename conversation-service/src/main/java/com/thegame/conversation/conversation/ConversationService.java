package com.thegame.conversation.conversation;

import com.thegame.conversation.entity.Conversation;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationDTO;
import com.thegame.dto.ConversationMessageDTO;
import com.thegame.dto.DetailedConversationDTO;
import com.thegame.request.ConversationMessageRequest;

import java.util.List;
import java.util.UUID;

public interface ConversationService {

    List<DetailedConversationDTO> getAllUserConversations(AuthenticationUserObject user);

    ConversationDTO getConversationById(UUID uuid, AuthenticationUserObject user);

    List<ConversationMessageDTO> getAllConversationMessages(UUID conversationId, AuthenticationUserObject user);

    String saveNewConversationMessage(UUID conversationId, AuthenticationUserObject user, ConversationMessageRequest request);

    Conversation createNewConversation(AuthenticationUserObject user, ConversationRequest request);

}
