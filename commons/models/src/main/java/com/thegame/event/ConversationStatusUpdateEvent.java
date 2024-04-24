package com.thegame.event;

import com.thegame.model.ConversationStatus;

import java.util.UUID;

public record ConversationStatusUpdateEvent(UUID conversationId, ConversationStatus status) {
}
