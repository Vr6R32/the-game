package com.thegame.websocket;

import java.util.UUID;

public record ChatMessage(UUID conversationId, String sender, String payload) {
}
