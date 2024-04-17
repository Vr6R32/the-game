package com.thegame.request;

import jakarta.validation.constraints.Email;

public record NewConversationRequest(@Email String secondUserEmail, String secondUserContactName) {
}
