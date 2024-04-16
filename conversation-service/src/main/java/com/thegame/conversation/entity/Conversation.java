package com.thegame.conversation.entity;

import com.thegame.model.ConversationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversations")
public class Conversation {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long firstUserId;

    private Long secondUserId;

    private Long lastMessageSenderId;

    private Long statusUpdatedByUserId;

    private String firstUserContactName;

    private String secondUserContactName;

    @Enumerated(value = EnumType.STRING)
    private ConversationStatus status;

    private Date lastMessageDate;

    private boolean isReadByReceiver;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    private List<ConversationMessage> messageList;

}
