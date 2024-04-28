package com.thegame.conversation.conversation;

import com.thegame.dto.*;
import com.thegame.request.ConversationMessageRequest;
import com.thegame.request.ConversationStatusUpdateRequest;
import com.thegame.request.NewConversationRequest;
import com.thegame.response.ConversationStatusUpdateResponse;
import com.thegame.response.NewConversationResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public Page<ConversationMessageDTO> getAllConversationMessages(@PathVariable("conversationId") UUID conversationId, Authentication authentication,
                                                                   @RequestParam(defaultValue = "20",required = false) int pageSize, @RequestParam(defaultValue = "-1") int pageNumber) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.getAllConversationMessages(conversationId, user, pageSize, pageNumber);
    }

    @PostMapping("/messages/{conversationId}/send")
    public Date sendAndSaveNewConversationMessage(@PathVariable("conversationId") UUID conversationId, @RequestBody ConversationMessageRequest request, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.saveNewConversationMessage(conversationId, user, request);
    }

    @PostMapping
    public NewConversationResponse createNewConversation(@RequestBody @Valid NewConversationRequest request, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.createNewConversation(user, request);
    }

    @PutMapping("status/update")
    public ConversationStatusUpdateResponse updateConversationStatus(@RequestBody ConversationStatusUpdateRequest request, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationFacade.updateConversationStatus(user, request);
    }
}

