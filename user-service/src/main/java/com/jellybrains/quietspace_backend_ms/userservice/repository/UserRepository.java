package com.jellybrains.quietspace_backend_ms.userservice.repository;


import com.jellybrains.quietspace_backend_ms.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
