package com.thegame.clients;


import com.thegame.dto.AppUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(value = "user-service")
public interface UserServiceClient {

    @PostMapping("api/v1/users/conversations/users/details")
    Map<UUID, AppUserDTO> getConversationsUsersDetails(@RequestHeader("X-USER-AUTH") String user, @RequestBody Map<UUID, Long> conversationIdSecondUserIdMap);

    @GetMapping("api/v1/users/{email}")
    Long getUserIdByEmailAddress(@RequestHeader("X-USER-AUTH") String user, @PathVariable("email") String email);
}
