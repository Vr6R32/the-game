package com.thegame.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewConversationRequest(@Email(message = "Bad Email Address")
                                     @NotBlank(message = "Fill up the form correctly") String secondUserEmail,
                                     @NotBlank(message = "Fill up the form correctly")
                                     @Size(max = 32,message = "Contact name too long") String secondUserContactName) {
}
