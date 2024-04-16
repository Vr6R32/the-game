package com.thegame.conversation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversation_messages")
public class ConversationMessage {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long senderId;

    private String payload;

    private Date messageSendDate;

    private Date messageReadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Conversation conversation;


}
