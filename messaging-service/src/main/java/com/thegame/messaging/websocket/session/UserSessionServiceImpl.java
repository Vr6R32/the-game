package com.thegame.messaging.websocket.session;

import com.thegame.messaging.websocket.filter.WebsocketUserPrincipal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UserSessionServiceImpl implements UserSessionService {


    private final UserSessionRepository userSessionRepository;

    @Override
    public void createSession(WebsocketUserPrincipal user) {
        UserSession newSession = UserSession.builder()
                .userId(user.userId())
                .username(user.name())
                .status(Status.ONLINE)
                .build();
        userSessionRepository.save(newSession);
    }

    @Override
    public void deleteSession(WebsocketUserPrincipal user) {
        userSessionRepository.deleteUserSessionByUsername(user.name());
    }
}
