package com.thegame.user;


import com.thegame.dto.AppUserDTO;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.request.NewConversationRequest;
import com.thegame.request.RegistrationRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/users")
record UserController(UserFacade userFacade) {


    @GetMapping
    public AuthenticationUserObject loggedUserAuthenticationDetails(Authentication authentication) {
        return (AuthenticationUserObject) authentication.getPrincipal();
    }

    @GetMapping("{id}")
    public AppUserDTO getAppUserDetailsById(@PathVariable("id") Long userId) {
        return userFacade.getAppUserDetailsById(userId);
    }

    @GetMapping("details/{email}")
    public AppUserDTO getAppUserDetailsByEmail(@PathVariable("email") String email) {
        return userFacade.getAppUserDetailsByEmail(email);
    }

    @GetMapping("{email}")
    public Long getUserIdByEmailAddress(@PathVariable("email") String email) {
        return userFacade.getUserIdByEmailAddress(email);
    }

    @PostMapping("register")
    public String registerNewAppUser(@RequestBody RegistrationRequest request) {
        return userFacade.registerNewAppUser(request);
    }

    @PostMapping("register/invitation")
    public AppUserDTO registerUserByInvitation(@RequestBody NewConversationRequest request, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return userFacade.registerUserByInvitation(request,user);
    }

    @PostMapping("/conversations/users/details")
    public Map<UUID, AppUserDTO> getConversationsUsersDetails(@RequestBody Map<UUID, Long> conversationIdSecondUserIdMap) {
        return userFacade.getConversationUserDetails(conversationIdSecondUserIdMap);
    }
}
