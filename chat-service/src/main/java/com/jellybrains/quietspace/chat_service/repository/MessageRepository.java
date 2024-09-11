package com.jellybrains.quietspace.chat_service.repository;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageRepository extends R2dbcRepository<Message, String> {

    Flux<Message> findAllByChatId(String chatId, Pageable pageable);

    Mono<Message> findFirstByChatOrderByCreateDateDesc(Chat chat);
}