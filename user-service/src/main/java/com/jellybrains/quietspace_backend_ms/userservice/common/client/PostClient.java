package com.jellybrains.quietspace_backend_ms.userservice.common.client;


import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.PostResponse;

import java.util.Optional;

public interface PostClient {

    Optional<PostResponse> getPostById(String postId);
}
