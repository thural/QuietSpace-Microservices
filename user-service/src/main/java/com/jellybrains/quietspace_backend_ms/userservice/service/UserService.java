package com.jellybrains.quietspace_backend_ms.userservice.service;

import com.jellybrains.quietspace_backend_ms.userservice.dto.requuest.UserRequest;
import com.jellybrains.quietspace_backend_ms.userservice.model.User;
import com.jellybrains.quietspace_backend_ms.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserRequest user) {
        User newUser = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

        userRepository.save(newUser);
        log.info("Created new user with email: {}", newUser.getEmail());
    }

}
