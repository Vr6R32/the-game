package com.thegame.response;

import com.thegame.dto.DetailedConversationDTO;
import org.springframework.http.HttpStatus;

public record NewConversationResponse(String message, HttpStatus status, DetailedConversationDTO conversation) {
}
