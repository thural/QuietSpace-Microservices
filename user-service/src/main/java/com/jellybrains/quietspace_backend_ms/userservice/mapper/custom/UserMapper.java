package com.jellybrains.quietspace_backend_ms.userservice.mapper.custom;

import com.jellybrains.quietspace_backend_ms.userservice.entity.User;
import com.jellybrains.quietspace_backend_ms.userservice.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public User userRequestToEntity(UserRequest userRequest){
        return User.builder()
                .role(userRequest.getRole())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }

    public UserResponse userEntityToResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createDate(user.getCreateDate())
                .updateDate(user.getUpdateDate())
                .build();
    };

    public UserRequest userEntityToRequest(User user){
        return null;
    };
}
