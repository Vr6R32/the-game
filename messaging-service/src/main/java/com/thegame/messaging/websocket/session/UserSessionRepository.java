package com.thegame.messaging.websocket.session;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserSessionRepository extends MongoRepository<UserSession, String> {

    void deleteUserSessionByUsername(String username);

}
