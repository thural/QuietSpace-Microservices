package com.jellybrains.quietspace_backend_ms.feedservice.service;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.FollowResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface FollowService {

    Page<FollowResponse> listFollowings(Integer pageNumber, Integer pageSize);

    Page<FollowResponse> listFollowers(Integer pageNumber, Integer pageSize);

    void toggleFollow(UUID followedUserId);
}
