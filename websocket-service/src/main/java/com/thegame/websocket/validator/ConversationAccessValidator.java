package com.thegame.websocket.validator;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationDTO;

public class ConversationAccessValidator {


    private ConversationAccessValidator() {
    }

    public static boolean validateConversationAccess(ConversationDTO conversation, AuthenticationUserObject senderUser) {
        return conversation.firstUserId().equals(senderUser.id()) || conversation.secondUserId().equals(senderUser.id());
    }
}
