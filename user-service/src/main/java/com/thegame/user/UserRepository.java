package com.thegame.user;

import com.thegame.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    @Query("SELECT u.id FROM AppUser u WHERE u.email = :email")
    Optional<Long> findUserIdByEmail(@Param("email") String email);
}
