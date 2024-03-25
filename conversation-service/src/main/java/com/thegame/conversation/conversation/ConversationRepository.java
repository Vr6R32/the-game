package com.thegame.conversation.conversation;

import com.thegame.conversation.entity.Conversation;
import com.thegame.conversation.entity.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findConversationIdById(UUID uuid);

    @Query("SELECT c FROM Conversation c WHERE c.firstUserId = :userId OR c.secondUserId = :userId")
    List<Conversation> findAllConversationsByUserId(@Param("userId") Long userId);

    @Query("SELECT cm FROM ConversationMessage cm WHERE cm.conversation.id = :conversationId " +
            "AND (cm.conversation.secondUserId = :userId OR cm.conversation.firstUserId = :userId) " +
            "ORDER BY cm.messageSendDate ASC")
    List<ConversationMessage> findAllConversationMessagesByUserAndConversationId(@Param("conversationId") UUID conversationId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Conversation c SET c.lastMessageDate = :lastMessageDate, c.lastMessageSenderId = :lastMessageSenderId, c.isReadByReceiver = :isReadByReceiver WHERE c.id = :conversationId")
    void updateLastMessageInfo(@Param("conversationId") UUID conversationId,
                               @Param("lastMessageDate") Date lastMessageDate,
                               @Param("lastMessageSenderId") Long lastMessageSenderId,
                               @Param("isReadByReceiver") boolean isReadByReceiver);

    @Modifying
    @Query("UPDATE Conversation c SET c.isReadByReceiver = true WHERE c.lastMessageSenderId != :receiverId")
    void updateMessagesReadByReceiver(@Param("receiverId") Long receiverId);
}
