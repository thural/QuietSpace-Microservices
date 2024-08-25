package com.jellybrains.quietspace.common_service.client.impl;

import com.jellybrains.quietspace.common_service.client.CommentClient;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentClientImpl implements CommentClient {

    private final WebClient webClient;
    private final String COMMENT_API_URI = "/api/v1/comments/";

    @Override
    public Optional<CommentResponse> getCommentById(String commentId) {
        return webClient.get()
                .uri(COMMENT_API_URI + commentId)
                .retrieve()
                .bodyToMono(CommentResponse.class)
                .blockOptional();
    }

}
