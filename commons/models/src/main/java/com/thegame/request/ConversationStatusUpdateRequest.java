package com.thegame.request;

import java.util.UUID;

public record ConversationStatusUpdateRequest(UUID conversationId, boolean isAccepted) {
}
