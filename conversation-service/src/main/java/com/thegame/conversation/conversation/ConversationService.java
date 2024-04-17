package com.thegame.conversation.conversation;

import com.thegame.dto.*;
import com.thegame.request.ConversationMessageRequest;
import com.thegame.request.NewConversationRequest;
import com.thegame.response.NewConversationResponse;

import java.util.List;
import java.util.UUID;

public interface ConversationService {

    List<DetailedConversationDTO> getAllUserConversations(AuthenticationUserObject user);

    List<ConversationFriendInfo> getAllUserConversationSecondUserIds(AuthenticationUserObject user);

    ConversationDTO getConversationById(UUID uuid, AuthenticationUserObject user);

    List<ConversationMessageDTO> getAllConversationMessages(UUID conversationId, AuthenticationUserObject user);

    String saveNewConversationMessage(UUID conversationId, AuthenticationUserObject user, ConversationMessageRequest request);

    NewConversationResponse createNewConversation(AuthenticationUserObject user, NewConversationRequest request);

}
