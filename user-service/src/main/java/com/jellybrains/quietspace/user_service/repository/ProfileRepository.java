package com.jellybrains.quietspace.user_service.repository;

import com.jellybrains.quietspace.user_service.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {

    Optional<Profile> findById(String profileId);

    Optional<Profile> findByUserId(String userId);

    Page<Profile> findAllByUsernameIsLikeIgnoreCase(String userName, Pageable pageable);

    Optional<Profile> findByEmail(String email);

    Page<Profile> findAllByQuery(String typedQuery, Pageable pageable);

    Optional<Profile> findByUsername(String username);

    Page<Profile> findAllByUserIdIn(List<String> userIds, Pageable pageable);

    void deleteByUserId(String userId);

    Page<Profile> findAll(Pageable pageable);

    void deleteById(String profileId);

    Profile save(Profile profile);

}
