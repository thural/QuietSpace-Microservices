package com.jellybrains.quietspace.chat_service.repository;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatRepository extends R2dbcRepository<Chat, String> {

    @Query("SELECT c FROM Chat c WHERE :userId MEMBER OF c.users")
    Flux<Chat> findChatsContainingUserId(@Param("userId") String userId);

    @Query("SELECT c FROM Chat c WHERE :userList = c.users")
    Flux<Chat> findAllByUsersContainingAll(@Param("userList") List<String> userList);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Chat c WHERE c.users = :userList")
    boolean existsByUsersContainingAll(@Param("userList") List<String> userList);

}
