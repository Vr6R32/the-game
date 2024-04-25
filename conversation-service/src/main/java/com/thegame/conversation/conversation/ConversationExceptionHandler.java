package com.thegame.conversation.conversation;

import com.thegame.response.ConversationStatusUpdateResponse;
import com.thegame.response.NewConversationResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ConversationExceptionHandler {

    @ExceptionHandler(ConversationException.class)
    public ConversationStatusUpdateResponse handleConversationException(ConversationException ex) {
        return new ConversationStatusUpdateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public NewConversationResponse handleValidationException(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        return new NewConversationResponse(errorMessage, HttpStatus.BAD_REQUEST, null);
    }
}