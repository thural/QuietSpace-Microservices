package com.jellybrains.quietspace_backend_ms.chatservice.repository;

import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByUserId(UUID userId);

    List<Chat> findAllByUserIdsIn(List<UUID> userList);
}
