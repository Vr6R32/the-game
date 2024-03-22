package com.thegame.user;


import com.thegame.dto.AppUserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/users")
record UserController(UserFacade userFacade) {


    @GetMapping("{id}")
    public AppUserDTO getAppUserDetails(@PathVariable("id") Long userId) {
        return userFacade.getAppUserDetails(userId);
    }

    @PostMapping("/conversations/users/details")
    public Map<UUID, AppUserDTO> getConversationsUsersDetails(@RequestBody Map<UUID, Long> conversationIdSecondUserIdMap) {
        return userFacade.getConversationUserDetails(conversationIdSecondUserIdMap);
    }
}
