package com.thegame.dto;

import java.util.Date;
import java.util.UUID;

public record ConversationDTO(UUID id, Long firstUserId, Long secondUserId, Long lastMessageSenderId, boolean isReadByReceiver, Date lastMessageDate) {
}
