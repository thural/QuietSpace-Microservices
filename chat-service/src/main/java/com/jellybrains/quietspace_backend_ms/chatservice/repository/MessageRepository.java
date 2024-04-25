package com.jellybrains.quietspace_backend_ms.chatservice.repository;

import com.jellybrains.quietspace_backend_ms.chatservice.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    Page<Message> findAllByChatId(UUID chatId, Pageable pageable);

}
