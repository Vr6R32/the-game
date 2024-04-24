package com.thegame.conversation.conversation;

import com.thegame.response.ConversationStatusUpdateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ConversationExceptionHandler {

    @ExceptionHandler(ConversationException.class)
    public ConversationStatusUpdateResponse handleConversationException(ConversationException ex) {
        return new ConversationStatusUpdateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }
}