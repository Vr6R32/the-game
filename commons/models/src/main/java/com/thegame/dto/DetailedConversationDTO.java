package com.thegame.dto;

import com.thegame.model.Status;

import java.util.Date;
import java.util.UUID;

public record DetailedConversationDTO(UUID id,
                                      Long userId,
                                      String userAvatarUrl,
                                      String userEmail,
                                      Status userStatus,
                                      Date userLogoutDate,
                                      Date lastMessageDate,
                                      boolean isUnread) {
}
