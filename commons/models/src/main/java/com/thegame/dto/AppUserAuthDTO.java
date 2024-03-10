package com.thegame.dto;

import com.thegame.model.Role;
import lombok.Builder;

@Builder
public record AppUserAuthDTO(
        Long id,
        String username,
        String password,
        String email,
        Role role,
        boolean accountEnabled,
        boolean accountNotLocked,
        boolean accountNotExpired,
        boolean credentialsNotExpired

) {
}
