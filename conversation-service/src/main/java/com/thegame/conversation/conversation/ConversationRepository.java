package com.thegame.conversation.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findConversationIdById(UUID uuid);

    @Query("SELECT c FROM Conversation c WHERE c.firstUserId = :userId OR c.secondUserId = :userId")
    List<Conversation> findAllConversationsByUserId(@Param("userId") Long userId);

    @Query("SELECT cm FROM ConversationMessage cm WHERE cm.conversation.id = :conversationId AND (cm.conversation.secondUserId = :userId OR cm.conversation.firstUserId = :userId)")
    List<ConversationMessage> findAllConversationMessagesByUserAndConversationId(@Param("conversationId") UUID conversationId, @Param("userId") Long userId);

}
