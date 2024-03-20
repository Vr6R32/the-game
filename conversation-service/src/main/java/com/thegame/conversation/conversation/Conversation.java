package com.thegame.conversation.conversation;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    private UUID id;

    private String ownerName;

    private String subOwnerName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    private List<ConversationMessage> messageList;

}
