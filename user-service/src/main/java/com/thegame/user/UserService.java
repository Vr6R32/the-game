package com.thegame.user;

import com.thegame.dto.AppUserDTO;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.request.NewConversationRequest;
import com.thegame.request.RegistrationRequest;

import java.util.Map;
import java.util.UUID;

public interface UserService {

    AppUserDTO getAppUserDetailsById(Long userId);

    Map<UUID, AppUserDTO> getConversationsUsersDetails(Map<UUID, Long> conversationIdSecondUserIdMap);

    Long getUserIdByEmailAddress(String email);

    String registerNewAppUser(RegistrationRequest request);

    AppUserDTO registerUserByInvitation(NewConversationRequest request, AuthenticationUserObject user);

    AppUserDTO getAppUserDetailsByEmail(String email);

}
