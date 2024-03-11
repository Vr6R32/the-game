package com.thegame.mapper;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;

public class UserMapper {

    private UserMapper() {
    }

    public static AppUser mapToUserEntity(AuthenticationUserObject userObject) {
        return AppUser.builder()
                .id(userObject.id())
                .username(userObject.username())
                .email(userObject.email())
                .role(userObject.role())
                .build();
    }
    public static AuthenticationUserObject mapUserEntityToAuthObject(AppUser user) {
        return AuthenticationUserObject.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
