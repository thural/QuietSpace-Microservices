package com.jellybrains.quietspace_backend_ms.chatservice.repository;

import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, String> {
    List<Chat> findAllByUsersId(String userId);

    List<Chat> findAllByUsersIn(List<String> userList);
}
