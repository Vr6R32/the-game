package com.thegame.dto;

import java.util.UUID;

public record ConversationMessageDTO(UUID id, Long senderId, String payload) {
}
