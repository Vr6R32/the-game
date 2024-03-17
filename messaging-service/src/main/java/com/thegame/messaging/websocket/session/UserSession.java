package com.thegame.messaging.websocket.session;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@Document
class UserSession {
    @Id
    private String id;
    private String username;
    private Long userId;
    private Status status;
}
