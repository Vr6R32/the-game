package com.thegame.clients;

import com.thegame.dto.UserSessionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.UUID;

@FeignClient(value = "websocket-service")
public interface WebSocketSessionClient {

    @PostMapping("api/v1/sessions")
    Map<UUID, UserSessionDTO> findConversationUserSessionsByIdMap(@RequestHeader("X-USER-AUTH") String user, @RequestBody Map<UUID, Long> conversationIdSecondUserIdMap);

}

