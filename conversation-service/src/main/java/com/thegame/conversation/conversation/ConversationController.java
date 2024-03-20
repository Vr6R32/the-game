package com.thegame.conversation.conversation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationRepository conversationRepository;

    @GetMapping
    public UUID findConversation(UUID uuid){
        return conversationRepository.findConversationIdById(uuid)
                .orElseGet(null);
    }

//    @PostMapping UUID sendPrivateMessage(MessageRequest request){
//        return conversationRepository.findConversationIdById();
//    }
}
