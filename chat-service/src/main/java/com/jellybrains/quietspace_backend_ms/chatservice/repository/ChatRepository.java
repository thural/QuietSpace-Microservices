package com.jellybrains.quietspace_backend_ms.chatservice.repository;

import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, String> {

//    @Query("SELECT c FROM Chat c WHERE :userId MEMBER OF c.users")
//    List<Chat> findByUsersContainingUserId(@Param("userId")String userId);

//    @Query("SELECT c FROM Chat c WHERE :userList = c.users")
//    List<Chat> findAllByUsersContainingAll(@Param("userList")List<String> userList);
//
//    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
//            "FROM Chat c WHERE c.users = :userList")
//    boolean existsByUsersContainingAll(@Param("userList") List<String> userList);

}
