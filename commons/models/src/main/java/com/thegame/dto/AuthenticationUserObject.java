package com.thegame.dto;

import com.thegame.model.Role;
import lombok.Builder;

import java.util.Date;

@Builder
public record AuthenticationUserObject(Long id, String username, String email, Role role, Date accessTokenExpiration) {
}
