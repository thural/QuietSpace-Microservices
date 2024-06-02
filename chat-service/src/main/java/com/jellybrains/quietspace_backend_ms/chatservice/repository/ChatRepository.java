package com.jellybrains.quietspace_backend_ms.chatservice.repository;

import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    List<Chat> findAllByUserIdsContaining(UUID userId);

    List<Chat> findAllByUserIdsIn(Collection<@NotNull Set<UUID>> userIds);
}
