package com.thegame.user;

import com.thegame.AppUser;
import com.thegame.dto.AppUserDTO;
import com.thegame.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

record UserServiceImpl(UserRepository userRepository) implements UserService {

    @Override
    public AppUserDTO getAppUserDetails(Long userId) {
        return userRepository.findById(userId).map(UserMapper::mapUserToDTO).orElseGet(() -> null);
    }

    @Override
    public Map<UUID, AppUserDTO> getConversationsUsersDetails(Map<UUID, Long> conversationIdSecondUserIdMap) {
        Set<Long> userIds = new HashSet<>(conversationIdSecondUserIdMap.values());
        Map<Long, AppUserDTO> userIdToUserDetailsMap = getUserDetailsByIds(userIds);
        Map<UUID, AppUserDTO> conversationUserDetailsMap = new HashMap<>();
        for (Map.Entry<UUID, Long> entry : conversationIdSecondUserIdMap.entrySet()) {
            UUID conversationId = entry.getKey();
            Long userId = entry.getValue();
            AppUserDTO userDetails = userIdToUserDetailsMap.get(userId);
            conversationUserDetailsMap.put(conversationId, userDetails);
        }
        return conversationUserDetailsMap;
    }

    @Override
    public Long getUserIdByEmailAddress(String email) {
        return userRepository.findUserIdByEmail(email).orElseGet(() -> null);
    }


    private Map<Long, AppUserDTO> getUserDetailsByIds(Set<Long> userIds) {
        List<AppUser> users = userRepository.findAllById(userIds);
        return users.stream().collect(Collectors.toMap(AppUser::getId, UserMapper::mapUserToDTO));
    }
}
