package com.jellybrains.quietspace.common_service.webclient.client;

import com.jellybrains.quietspace.common_service.model.response.PostResponse;

import java.util.Optional;

public interface PostClient {

    Optional<PostResponse> getPostById(String postId);
}
