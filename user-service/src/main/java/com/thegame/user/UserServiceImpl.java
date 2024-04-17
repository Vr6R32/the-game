package com.thegame.user;

import com.thegame.AppUser;
import com.thegame.dto.AppUserDTO;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.mapper.UserMapper;
import com.thegame.model.Role;
import com.thegame.request.NewConversationRequest;
import com.thegame.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDTO getAppUserDetailsById(Long userId) {
        return userRepository.findById(userId).map(UserMapper::mapUserToDTO).orElseGet(() -> null);
    }

    @Override
    public Map<UUID, AppUserDTO> getConversationsUsersDetails(Map<UUID, Long> conversationIdSecondUserIdMap) {
        Set<Long> userIds = new HashSet<>(conversationIdSecondUserIdMap.values());
        Map<Long, AppUserDTO> userIdToUserDetailsMap = getUserDetailsByIds(userIds);
        Map<UUID, AppUserDTO> conversationUserDetailsMap = new HashMap<>();
        for (Map.Entry<UUID, Long> entry : conversationIdSecondUserIdMap.entrySet()) {
            UUID conversationId = entry.getKey();
            Long userId = entry.getValue();
            AppUserDTO userDetails = userIdToUserDetailsMap.get(userId);
            conversationUserDetailsMap.put(conversationId, userDetails);
        }
        return conversationUserDetailsMap;
    }

    @Override
    public Long getUserIdByEmailAddress(String email) {
        return userRepository.findUserIdByEmail(email).orElseGet(() -> null);
    }

    @Override
    public String registerNewAppUser(RegistrationRequest request) {

        if(getUserIdByEmailAddress(request.email()) != null) return "Email Address Already Used";

        //TODO SEND AN ACTIVATION LINK BY EMAIL SERVICE

        AppUser newUser = AppUser.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .avatarUrl("default")
                .accountEnabled(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialsNotExpired(true)
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(newUser);

        log.info("USER SERVICE -> SUCCESSFULLY REGISTERED USER FOR USERNAME [{}] , EMAIL [{}] , ASSIGNED ID [{}]",newUser.getUsername(),newUser.getEmail(),newUser.getId());

        return "Successfully Registered!";
    }

    @Override
    public Long registerUserByInvitation(NewConversationRequest request, AuthenticationUserObject user) {

        //TODO SEND AN INVITATION BY EMAIL SERVICE

        String registerCode = RandomStringUtils.randomAlphanumeric(30, 30);
        String randomPass = RandomStringUtils.randomAlphanumeric(30, 30);

        AppUser newUser = AppUser.builder()
                .email(request.secondUserEmail())
                .avatarUrl("default")
                .username(registerCode)
                .password(passwordEncoder.encode(randomPass))
                .accountEnabled(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialsNotExpired(true)
                .role(Role.ROLE_USER)
                .registerCode(registerCode)
                .build();

        userRepository.save(newUser);

        log.info("USER SERVICE -> SUCCESSFULLY REGISTERED USER FOR EMAIL [{}] , ASSIGNED ID [{}] INVITATION BY [{}] ",newUser.getEmail(),newUser.getId(),user.id());

        return newUser.getId();
    }

    @Override
    public AppUserDTO getAppUserDetailsByEmail(String email) {
        return userRepository.findUserDTOByEmail(email).map(UserMapper::mapUserToDTO).orElseGet(() -> null);
    }


    private Map<Long, AppUserDTO> getUserDetailsByIds(Set<Long> userIds) {
        List<AppUser> users = userRepository.findAllById(userIds);
        return users.stream().collect(Collectors.toMap(AppUser::getId, UserMapper::mapUserToDTO));
    }
}
