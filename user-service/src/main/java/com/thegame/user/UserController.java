package com.thegame.user;

import com.thegame.AppUser;
import com.thegame.dto.AppUserAuthDTO;
import com.thegame.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
record UserController(UserFacade userFacade, UserRepository userRepository, PasswordEncoder passwordEncoder) {


    @GetMapping("{username}")
    public AppUserAuthDTO getUserByName(@PathVariable("username") String username) {
        AppUser user = userRepository.findByUsername(username);

        return new AppUserAuthDTO(user.getId(),user.getUsername(), user.getPassword(),user.getEmail(), user.getRole(),user.isAccountEnabled(),user.isAccountNotLocked(),user.isAccountNotExpired(),user.isCredentialsNotExpired());
    }

    @GetMapping("generate")
    public void generateUser() {
        AppUser user = AppUser.builder()
                .name("michal")
                .surname("kowalkowki")
                .username("karacz")
                .email("karacz@hitman.pl")
                .role(Role.ROLE_USER)
                .password(passwordEncoder.encode("odi123"))
                .accountEnabled(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .accountEnabled(true)
                .credentialsNotExpired(true)
                .build();
        userRepository.save(user);
    }
}
