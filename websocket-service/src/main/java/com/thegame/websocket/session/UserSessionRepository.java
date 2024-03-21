package com.thegame.websocket.session;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserSessionRepository extends MongoRepository<UserSession, String> {

    void deleteUserSessionByUsername(String username);

    Optional<UserSession> findUserSessionByUserId(Long userId);

}
