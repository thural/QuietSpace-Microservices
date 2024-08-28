package com.jellybrains.quietspace.chat_service.webclient.service;

import com.jellybrains.quietspace.chat_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.chat_service.webclient.client.PostClient;
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
