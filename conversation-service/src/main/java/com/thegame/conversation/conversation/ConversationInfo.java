package com.thegame.conversation.conversation;

import java.util.Date;

public record ConversationInfo(Long secondUserId, Date lastMessageDate, boolean isUnread) {
}
