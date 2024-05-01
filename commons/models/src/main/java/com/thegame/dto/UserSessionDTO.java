package com.thegame.dto;

import com.thegame.model.Status;

import java.util.Date;

public record UserSessionDTO(String username, Long userId, Status status, Date logoutTime, String loggedInstanceId) {
}
