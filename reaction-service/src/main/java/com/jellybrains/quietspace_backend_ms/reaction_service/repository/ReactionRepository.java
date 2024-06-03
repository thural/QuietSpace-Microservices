package com.jellybrains.quietspace_backend_ms.reaction_service.repository;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.Reaction;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.LikeType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends MongoRepository<Reaction, String> {

    List<Reaction> findAllByContentId(String contentId);

    List<Reaction> findAllByUserId(String userId);

    List<Reaction> findAllByContentIdAndUserId(String contentId, String userId);

    boolean existsByContentIdAndUserId(String contentId, String userId);

    Optional<Reaction> findByContentIdAndUserId(String commentId, String userId);

    List<Reaction> findAllByContentTypeAndUserId(ContentType contentType, String userId);

    List<Reaction> findAllByContentIdAndContentType(String contentId, ContentType contentType);

    List<Reaction> findAllByUserIdAndContentType(String userId, ContentType contentType);

    List<Reaction> findAllByContentIdAndContentTypeAndLikeType(String contentId, ContentType type, LikeType likeType);

    Integer countByContentIdAndLikeType(String contentId, LikeType likeType);

    List<Reaction> findAllByContentIdAndLikeType(String contentId, LikeType likeType);

}