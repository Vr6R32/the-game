package com.thegame.conversation.conversation;

import com.thegame.conversation.entity.Conversation;
import com.thegame.dto.*;
import com.thegame.request.ConversationMessageRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/conversations")
public record ConversationController(ConversationFacade conversationFacade) {

    @GetMapping
    public List<DetailedConversationDTO> getAllDetailedUserConversations(Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.getAllUserConversations(user);
    }

    @GetMapping("users/ids")
    public List<ConversationFriendInfo> getAllUserConversationFriendIds(Authentication authentication){
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.getAllUserConversationSecondUserIds(user);
    }

    @GetMapping("{uuid}")
    public ConversationDTO getConversationById(@PathVariable UUID uuid,Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.getConversationById(uuid,user);
    }

    @GetMapping("/messages/{conversationId}")
    public List<ConversationMessageDTO> getAllConversationMessages(@PathVariable("conversationId") UUID conversationId, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.getAllConversationMessages(conversationId, user);
    }

    @PostMapping("/messages/{conversationId}/send")
    public String sendAndSaveNewConversationMessage(@PathVariable("conversationId") UUID conversationId, @RequestBody ConversationMessageRequest request, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.saveNewConversationMessage(conversationId, user, request);
    }

    @PostMapping
    public Conversation createNewConversation(@RequestBody @Valid ConversationRequest request, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.createNewConversation(user, request);
    }
}

