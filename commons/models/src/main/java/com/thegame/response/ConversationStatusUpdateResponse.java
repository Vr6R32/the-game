package com.thegame.response;

import com.thegame.model.ConversationStatus;
import org.springframework.http.HttpStatus;

public record ConversationStatusUpdateResponse(String message, HttpStatus status, ConversationStatus conversationStatus) {
}
