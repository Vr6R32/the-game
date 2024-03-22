package com.thegame.dto;

import java.util.UUID;

public record DetailedConversationDTO(UUID id, Long secondUserId, String secondUserAvatarUrl, String secondUserEmail) {
}
