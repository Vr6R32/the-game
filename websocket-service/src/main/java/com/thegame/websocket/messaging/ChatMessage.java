package com.thegame.websocket.messaging;

import java.util.Date;
import java.util.UUID;

public record ChatMessage(UUID conversationId, Long senderId, String message, Date messageDate, Long receiverId) {
}
