package com.thegame.websocket.notification;

import com.thegame.model.Status;

import java.util.Date;
import java.util.UUID;

public record SessionStatusUpdateEvent(UUID conversationId, Status status, Date eventDate) {
}
