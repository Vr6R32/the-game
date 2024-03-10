package com.thegame.security;

import com.thegame.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
