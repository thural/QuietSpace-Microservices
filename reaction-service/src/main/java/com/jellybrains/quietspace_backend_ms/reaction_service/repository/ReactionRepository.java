package com.jellybrains.quietspace_backend_ms.reaction_service.repository;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.Reaction;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.LikeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReactionRepository extends MongoRepository<Reaction, String> {

    Page<Reaction> findAllByContentId(String contentId, PageRequest pageRequest);

    Page<Reaction> findAllByUserId(String userId, PageRequest pageRequest);

    Page<Reaction> findAllByContentIdAndUserId(String contentId, String userId, PageRequest pageRequest);

    boolean existsByContentIdAndUserId(String contentId, String userId);

    Optional<Reaction> findByContentIdAndUserId(String commentId, String userId);

    Page<Reaction> findAllByContentTypeAndUserId(ContentType contentType, String userId, PageRequest pageRequest);

    Page<Reaction> findAllByContentIdAndContentType(String contentId, ContentType contentType, PageRequest pageRequest);

    Page<Reaction> findAllByUserIdAndContentType(String userId, ContentType contentType, PageRequest pageRequest);

    Page<Reaction> findAllByContentIdAndContentTypeAndLikeType(String contentId, ContentType type, LikeType likeType, PageRequest pageRequest);

    Integer countByContentIdAndLikeType(String contentId, LikeType likeType);

    Page<Reaction> findAllByContentIdAndLikeType(String contentId, LikeType likeType, PageRequest pageRequest);

}