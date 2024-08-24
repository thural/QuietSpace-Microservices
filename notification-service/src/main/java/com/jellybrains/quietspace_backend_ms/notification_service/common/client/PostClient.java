package com.jellybrains.quietspace_backend_ms.notification_service.common.client;

import com.jellybrains.quietspace_backend_ms.notification_service.common.model.response.PostResponse;

import java.util.Optional;

public interface PostClient {

    Optional<PostResponse> getPostById(String postId);
}
