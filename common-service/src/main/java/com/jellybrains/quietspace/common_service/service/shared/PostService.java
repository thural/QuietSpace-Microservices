package com.jellybrains.quietspace.common_service.service.shared;

import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.common_service.webclient.client.PostClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class PostService {

    private final PostClient postClient;

    public CompletableFuture<Optional<PostResponse>> getPostById(String postId) {
        return postClient.getPostById(postId);
    }

    public CompletableFuture<String> getUserIdByPostId(String postId) {
        return getPostById(postId)
                .thenApply(optional -> optional.map(PostResponse::getUserId).orElseThrow());
    }
}
