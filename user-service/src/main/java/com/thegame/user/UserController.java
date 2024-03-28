package com.thegame.user;


import com.thegame.dto.AppUserDTO;
import com.thegame.dto.AuthenticationUserObject;
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

//    @GetMapping("{id}")
//    public AppUserDTO getAppUserDetails(@PathVariable("id") Long userId) {
//        return userFacade.getAppUserDetails(userId);
//    }

    @GetMapping("{email}")
    public Long getUserIdByEmailAddress(@PathVariable("email") String email) {
        return userFacade.getUserIdByEmailAddress(email);
    }

    @PostMapping("/conversations/users/details")
    public Map<UUID, AppUserDTO> getConversationsUsersDetails(@RequestBody Map<UUID, Long> conversationIdSecondUserIdMap) {
        return userFacade.getConversationUserDetails(conversationIdSecondUserIdMap);
    }
}
