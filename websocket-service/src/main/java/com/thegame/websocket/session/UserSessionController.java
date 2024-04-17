package com.thegame.websocket.session;

import com.thegame.dto.UserSessionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/sessions")
record UserSessionController(UserSessionFacade sessionFacade) {


    @PostMapping
    public Map<UUID, UserSessionDTO>  findUserSessionDetailsByIdsMap(@RequestBody Map<UUID, Long> conversationIdSecondUserIdMap) {
        return sessionFacade.findUserSessionDetailsByIdsMap(conversationIdSecondUserIdMap);
    }

    @GetMapping("{userId}")
    public UserSessionDTO findUserSessionDetailsById(@PathVariable Long userId) {
        return sessionFacade.findUserSessionByUserId(userId);
    }
}
