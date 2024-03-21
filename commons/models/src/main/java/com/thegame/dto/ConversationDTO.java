package com.thegame.dto;

import java.util.UUID;

public record ConversationDTO(UUID id, Long firstUserId, Long secondUserId) {
}
