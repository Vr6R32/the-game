package com.thegame.conversation.conversation;

import com.thegame.model.ConversationStatus;

import java.util.Date;

public record ConversationInfo(Long secondUserId, Date lastMessageDate, String secondUserContactName,
                               ConversationStatus status, boolean isUnread) {
}
