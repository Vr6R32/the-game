package com.thegame.websocket.session;

import com.thegame.model.Status;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Builder
@Document
public class UserSession {

    @Id
    private String id;
    private String username;
    private Long userId;
    private Status status;
    private Date logoutTime;
}
