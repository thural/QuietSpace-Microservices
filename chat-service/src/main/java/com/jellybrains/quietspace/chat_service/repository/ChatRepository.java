package com.jellybrains.quietspace.chat_service.repository;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatRepository extends ReactiveCrudRepository<Chat, String> {

    @Query("SELECT c FROM Chat c WHERE :userId MEMBER OF c.users")
    Flux<Chat> findChatsContainingUserId(@Param("userId") String userId);

    @Query("SELECT c FROM Chat c WHERE :userList = c.users")
    Flux<Chat> findAllByUsersContainingAll(@Param("userList") List<String> userList);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Chat c WHERE c.users = :userList")
    Mono<Boolean> existsByUsersContainingAll(@Param("userList") List<String> userList);

}
