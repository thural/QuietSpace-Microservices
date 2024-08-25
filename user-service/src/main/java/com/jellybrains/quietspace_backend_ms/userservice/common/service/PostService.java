package com.jellybrains.quietspace_backend_ms.userservice.common.service;

import com.jellybrains.quietspace_backend_ms.userservice.common.client.PostClient;
import com.jellybrains.quietspace_backend_ms.userservice.common.exception.CustomNotFoundException;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.PostResponse;
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
