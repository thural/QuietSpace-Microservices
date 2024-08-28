package com.jellybrains.quietspace.chat_service.repository;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, String> {

    Page<Message> findAllByChatId(String chatId, Pageable pageable);
    Optional<Message> findFirstByChatOrderByCreateDateDesc(Chat chat);
}