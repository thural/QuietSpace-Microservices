package com.jellybrains.quietspace_backend_ms.authorization_service.repository;

import com.jellybrains.quietspace_backend_ms.authorization_service.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    boolean existsByToken(String jwtToken);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Optional<Token> getByEmail(String email);

    Optional<Token> findByToken(String token);
}
