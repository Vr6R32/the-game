package com.thegame.websocket.session;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public
interface UserSessionRepository extends MongoRepository<UserSession, String> {

    Optional<UserSession> findUserSessionByUserId(Long userId);

    List<UserSession> findAllByUserIdIn(Set<Long> userIds);
}
