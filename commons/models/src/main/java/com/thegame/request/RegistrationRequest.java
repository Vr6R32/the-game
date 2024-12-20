package com.thegame.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @Email String email,
        @NotBlank String username,
        @NotBlank String password) {
}
