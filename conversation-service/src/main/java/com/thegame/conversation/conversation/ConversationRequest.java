package com.thegame.conversation.conversation;

import jakarta.validation.constraints.Email;

public record ConversationRequest(@Email String secondUserEmail) {
}
