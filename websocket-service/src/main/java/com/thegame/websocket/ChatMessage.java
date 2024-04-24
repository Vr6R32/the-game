package com.thegame.websocket;

import java.util.Date;
import java.util.UUID;

public record ChatMessage(UUID conversationId, Long senderId, String payload, Date messageDate) {
}
