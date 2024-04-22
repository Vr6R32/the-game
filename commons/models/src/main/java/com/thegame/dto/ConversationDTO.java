package com.thegame.dto;

import com.thegame.model.ConversationStatus;

import java.util.Date;
import java.util.UUID;

public record ConversationDTO(UUID id, Long firstUserId, Long secondUserId,
                              Long lastMessageSenderId, boolean isReadByReceiver, Date lastMessageDate,
                              String firstUserContactName, String secondUserContactName, ConversationStatus status) {
}
