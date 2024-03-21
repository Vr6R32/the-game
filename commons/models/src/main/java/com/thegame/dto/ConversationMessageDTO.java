package com.thegame.dto;

import java.util.UUID;

public record ConversationMessageDTO(UUID id, String sender, String payload) {
}
