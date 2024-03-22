package com.thegame.user;

import com.thegame.dto.AppUserDTO;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    AppUserDTO getAppUserDetails(Long userId);

    Map<UUID, AppUserDTO> getConversationsUsersDetails(Map<UUID, Long> conversationIdSecondUserIdMap);

}
