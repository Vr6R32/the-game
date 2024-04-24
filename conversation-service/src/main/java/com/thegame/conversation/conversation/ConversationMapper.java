package com.thegame.conversation.conversation;

import com.thegame.conversation.entity.Conversation;
import com.thegame.conversation.entity.ConversationMessage;
import com.thegame.dto.ConversationDTO;
import com.thegame.dto.ConversationMessageDTO;

public class ConversationMapper {

    private ConversationMapper() {
    }

    public static ConversationDTO mapConversationToDTO(Conversation conversation) {
        return new ConversationDTO(
                conversation.getId(),
                conversation.getFirstUserId(),
                conversation.getSecondUserId(),
                conversation.getLastMessageSenderId(),
                conversation.isReadByReceiver(),
                conversation.getLastMessageDate(),
                conversation.getFirstUserContactName(),
                conversation.getSecondUserContactName(),
                conversation.getStatus(),
                conversation.getStatusUpdatedByUserId());
    }
    public static ConversationMessageDTO mapConversationMessageToDTO(ConversationMessage message) {
        return new ConversationMessageDTO(message.getId(), message.getSenderId(), message.getPayload());
    }
}
