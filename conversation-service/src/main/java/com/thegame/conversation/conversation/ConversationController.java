package com.thegame.conversation.conversation;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.ConversationDTO;
import com.thegame.dto.ConversationMessageDTO;
import com.thegame.request.ConversationMessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/conversation")
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final ConversationMessageRepository messageRepository;


    @GetMapping
    public List<ConversationDTO> getAllUserConversations(Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationRepository.findAllConversationsByUserId(user.id())
                .stream()
                .map(ConversationMapper::mapConversationToDTO)
                .toList();
    }

    @GetMapping("{uuid}")
    public ConversationDTO getConversationById(@PathVariable UUID uuid){
        return conversationRepository.findConversationIdById(uuid)
                .map(ConversationMapper::mapConversationToDTO).orElseGet(null);
    }

    @GetMapping("/messages/{conversationId}")
    public List<ConversationMessageDTO> getAllConversationMessages(@PathVariable("conversationId") UUID conversationId, Authentication authentication) {
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        return conversationRepository.findAllConversationMessagesByUserAndConversationId(conversationId, user.id())
                .stream()
                .map(ConversationMapper::mapConversationMessageToDTO)
                .toList();
    }

    @PostMapping("/messages/{conversationId}/send")
    public String sendAndSaveNewConversationMessage(@PathVariable("conversationId") UUID conversationId,
                                                                        @RequestBody ConversationMessageRequest request, Authentication authentication){
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();

        ConversationMessage newMessage = ConversationMessage.builder()
                .conversation(conversationRepository.getReferenceById(conversationId))
                .sender(user.username())
                .payload(request.payload())
                .build();

        messageRepository.save(newMessage);

        return "OK";
    }

    @PostMapping
    public Conversation createNewConversation(@RequestBody @Valid ConversationRequest request, Authentication authentication){
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        // USER SERVICE FIND BY EMAIL , IF NOT EXISTS SEND AN INVITATION

        Conversation newConversation = Conversation.builder()
                .firstUserId(user.id())
                .secondUserId(2L)
                .build();

        return conversationRepository.save(newConversation);
    }

//    @PostMapping UUID sendPrivateMessage(MessageRequest request){
//        return conversationRepository.findConversationIdById();
//    }
}
