package com.thegame.authservice.security;


import com.thegame.AppUser;
import com.thegame.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class UserRepositoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public AppUser findByUsername(String username) {
        String sql = "SELECT id, username, password, role, email, account_enabled, account_not_locked, account_not_expired, credentials_not_expired FROM app_user WHERE username = ?";

        return jdbcTemplate.query(sql, preparedStatement ->
                preparedStatement.setString(1, username), this::mapToAppUser);
    }

    private AppUser mapToAppUser(ResultSet resultSet) throws SQLException {
        AppUser user = new AppUser();
        if (resultSet.next()) {
            user.setId(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
            user.setEmail(resultSet.getString("email"));
            user.setAccountEnabled(resultSet.getBoolean("account_enabled"));
            user.setAccountNotLocked(resultSet.getBoolean("account_not_locked"));
            user.setAccountNotExpired(resultSet.getBoolean("account_not_expired"));
            user.setCredentialsNotExpired(resultSet.getBoolean("credentials_not_expired"));
        }
        return user;
    }
}