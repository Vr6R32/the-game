package com.thegame.clients;

import com.thegame.config.FeignClientConfig;
import com.thegame.dto.UserSessionDTO;
import com.thegame.model.Notification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(value = "websocket-service", configuration = FeignClientConfig.class)
public interface WebSocketSessionClient {

    @PostMapping("api/v1/sessions")
    Map<UUID, UserSessionDTO> findConversationUserSessionsByIdMap(@RequestHeader("X-USER-AUTH") String user, @RequestBody Map<UUID, Long> conversationIdSecondUserIdMap);

    @GetMapping("api/v1/sessions/{userId}")
    UserSessionDTO findUserSessionDetailsById(@RequestHeader("X-USER-AUTH") String user, @PathVariable("userId") Long userId);

    @PostMapping("api/v1/ws/notifications/{secondUserId}")
    void sendNewConversationNotificationEvent(@RequestHeader("X-USER-AUTH") String user, Notification notification, @PathVariable("secondUserId") Long secondUserId);
}

