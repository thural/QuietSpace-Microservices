package com.jellybrains.quietspace_backend_ms.userservice.repository;


import com.jellybrains.quietspace_backend_ms.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findUserByEmail(String email);
}
