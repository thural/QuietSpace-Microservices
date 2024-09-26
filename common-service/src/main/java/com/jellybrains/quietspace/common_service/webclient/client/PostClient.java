package com.jellybrains.quietspace.common_service.webclient.client;

import com.jellybrains.quietspace.common_service.model.response.PostResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PostClient {

    CompletableFuture<Optional<PostResponse>> getPostById(String postId);
}
