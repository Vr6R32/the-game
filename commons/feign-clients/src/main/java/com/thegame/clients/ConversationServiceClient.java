package com.thegame.clients;

import com.thegame.dto.ConversationDTO;
import com.thegame.request.ConversationMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;


@FeignClient(name = "conversation-service")
public interface ConversationServiceClient {

    @GetMapping("api/v1/conversation/{uuid}")
    ConversationDTO findConversationById(@RequestHeader("X-USER-AUTH") String user, @PathVariable("uuid") UUID uuid);

    @PostMapping("api/v1/conversation/messages/{conversationId}/send")
    String sendAndSaveNewConversationMessage(@RequestHeader("X-USER-AUTH") String user, @PathVariable("conversationId") UUID conversationId, ConversationMessageRequest request);
}

