package com.jellybrains.quietspace_backend_ms.reaction_service.common.service;

import com.jellybrains.quietspace_backend_ms.reaction_service.common.client.PostClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.exception.CustomNotFoundException;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.model.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostService {

    private final PostClient postClient;

    public PostResponse getPostById(String postId){
        return postClient.getPostById(postId)
                .orElseThrow(CustomNotFoundException::new);
    }

    public String getUserIdByPostId(String postId){
        return getPostById(postId).getUserId(); // TODO: use kafka instead
    }
}
