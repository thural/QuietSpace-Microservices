package com.jellybrains.quietspace_backend_ms.authorization_service.repository;

import com.jellybrains.quietspace_backend_ms.authorization_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
