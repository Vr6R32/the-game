package com.thegame.user;

import com.thegame.dto.AppUserDTO;

import java.util.Map;
import java.util.UUID;

record UserFacade(UserService userService) {


    public AppUserDTO getAppUserDetails(Long userId) {
        return userService.getAppUserDetails(userId);
    }

    public Map<UUID, AppUserDTO> getConversationUserDetails(Map<UUID, Long> conversationIdSecondUserIdMap) {
        return userService.getConversationsUsersDetails(conversationIdSecondUserIdMap);
    }

    public Long getUserIdByEmailAddress(String email) {
        return userService.getUserIdByEmailAddress(email);
    }
}
