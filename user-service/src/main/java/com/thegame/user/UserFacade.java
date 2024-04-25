package com.thegame.user;

import com.thegame.dto.AppUserDTO;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.request.NewConversationRequest;
import com.thegame.request.RegistrationRequest;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

record UserFacade(UserService userService) {


    public AppUserDTO getAppUserDetailsById(Long userId) {
        return userService.getAppUserDetailsById(userId);
    }

    public Map<UUID, AppUserDTO> getConversationUserDetails(Map<UUID, Long> conversationIdSecondUserIdMap) {
        return userService.getConversationsUsersDetails(conversationIdSecondUserIdMap);
    }

    public Long getUserIdByEmailAddress(String email) {
        return userService.getUserIdByEmailAddress(email);
    }

    public String registerNewAppUser(@Valid RegistrationRequest request) {
        return userService.registerNewAppUser(request);
    }

    public AppUserDTO registerUserByInvitation(NewConversationRequest request, AuthenticationUserObject user) {
        return userService.registerUserByInvitation(request,user);
    }

    public AppUserDTO getAppUserDetailsByEmail(String email) {
        return userService.getAppUserDetailsByEmail(email);
    }
}
