package com.jellybrains.quietspace.auth_service.repository;

import com.jellybrains.quietspace.auth_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findAllByUsernameIsLikeIgnoreCase(String userName, Pageable pageable);

    Optional<User> findUserEntityByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:query% OR u.email LIKE %:query%")
    Page<User> findAllByQuery(String query, PageRequest pageRequest);

    Optional<User> findUserByUsername(String username);
}
