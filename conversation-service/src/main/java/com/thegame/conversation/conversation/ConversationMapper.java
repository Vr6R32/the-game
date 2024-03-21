package com.thegame.conversation.conversation;

import com.thegame.dto.ConversationDTO;
import com.thegame.dto.ConversationMessageDTO;

public class ConversationMapper {

    private ConversationMapper() {
    }

    public static ConversationDTO mapConversationToDTO(Conversation conversation) {
        return new ConversationDTO(conversation.getId(), conversation.getFirstUserId(), conversation.getSecondUserId());
    }
    public static ConversationMessageDTO mapConversationMessageToDTO(ConversationMessage message) {
        return new ConversationMessageDTO(message.getId(), message.getSender(), message.getPayload());
    }
}
