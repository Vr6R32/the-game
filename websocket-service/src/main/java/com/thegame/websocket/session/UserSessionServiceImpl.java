package com.thegame.websocket.session;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.UserSessionDTO;
import com.thegame.model.Status;
import com.thegame.websocket.eureka.EurekaClientInfo;
import com.thegame.websocket.filter.WebsocketUserPrincipal;
import com.thegame.websocket.notification.NotificationFacade;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
class UserSessionServiceImpl implements UserSessionService {

    private final NotificationFacade notificationFacade;
    private final UserSessionRepository userSessionRepository;
    private final EurekaClientInfo eurekaClientInfo;

    @Override
    public void setSessionStatusOnline(WebsocketUserPrincipal user) {

        UserSession userSession = userSessionRepository.findUserSessionByUserId(user.userId()).orElseGet(() -> null);
        String instanceId = eurekaClientInfo.getInstanceId();


        if(userSession == null) {
            UserSession newSession = UserSession.builder()
                    .userId(user.userId())
                    .username(user.name())
                    .status(Status.ONLINE)
                    .loggedInstanceId(instanceId)
                    .build();
            userSessionRepository.save(newSession);
        } else {
            userSession.setLoggedInstanceId(instanceId);
            userSession.setStatus(Status.ONLINE);
            userSession.setLogoutTime(null);
            userSessionRepository.save(userSession);
        }

        notificationFacade.sendUpdateSessionStatusEventToConversationFriends(user, Status.ONLINE);
    }

    @Override
    public void setSessionStatusOffline(WebsocketUserPrincipal user) {
        UserSession userSession = userSessionRepository.findUserSessionByUserId(user.userId()).orElseGet(() -> null);

        if(userSession != null && !userSession.getStatus().equals(Status.RECONNECTING)) {
            userSession.setStatus(Status.OFFLINE);
            userSession.setLogoutTime(Date.from(Instant.now().plus(Duration.ofHours(2))));
            userSession.setLoggedInstanceId(null);
            userSessionRepository.save(userSession);
            notificationFacade.sendUpdateSessionStatusEventToConversationFriends(user, Status.OFFLINE);

        }
    }

    @Override
    public void setSessionStatusReconnect(AuthenticationUserObject user) {
        UserSession userSession = userSessionRepository.findUserSessionByUserId(user.id()).orElseGet(() -> null);

        if(userSession!=null) {
            userSession.setStatus(Status.RECONNECTING);
            userSessionRepository.save(userSession);
        }
    }

    public UserSessionDTO findUserSessionByUserId(Long userId){
        return userSessionRepository.findUserSessionByUserId(userId)
                .map(UserSessionMapper::mapUserSessionToDTO)
                .orElseGet(() -> null);
    }

    @Override
    public Map<UUID, UserSessionDTO> findUserSessionDetailsByIdsMap(Map<UUID, Long> conversationIdSecondUserIdMap) {

        Set<Long> userIds = new HashSet<>(conversationIdSecondUserIdMap.values());
        Map<Long, UserSessionDTO> userIdToUserDetailsMap = getUserDetailsByIds(userIds);
        Map<UUID, UserSessionDTO> conversationUserSessionDetailsMap = new HashMap<>();

        for (Map.Entry<UUID, Long> entry : conversationIdSecondUserIdMap.entrySet()) {
            UUID conversationId = entry.getKey();
            Long userId = entry.getValue();
            UserSessionDTO userSessionDetails = userIdToUserDetailsMap.get(userId);
            conversationUserSessionDetailsMap.put(conversationId, userSessionDetails);
        }
        return conversationUserSessionDetailsMap;
    }


    private Map<Long, UserSessionDTO> getUserDetailsByIds(Set<Long> userIds) {
        List<UserSession> users = userSessionRepository.findAllByUserIdIn(userIds);
        return users.stream().collect(Collectors.toMap(UserSession::getUserId, UserSessionMapper::mapUserSessionToDTO));
    }
}
