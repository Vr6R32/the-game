package com.thegame.dto;

import lombok.Builder;


@Builder
public record AppUserDTO(Long id, String email, String avatarUrl) {
}
