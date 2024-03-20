package com.thegame.websocket.session;

import com.thegame.websocket.filter.WebsocketUserPrincipal;
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

    public UserSession findUserSessionByUsername(String username){


        return userSessionRepository.findUserSessionByUsername(username).orElse(null);
    }
}
