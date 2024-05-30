package com.jellybrains.quietspace_backend_ms.userservice.mapper;

import com.jellybrains.quietspace_backend_ms.userservice.entity.User;
import com.jellybrains.quietspace_backend_ms.userservice.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User userRequestToEntity(UserRequest userRequest);

    UserResponse userEntityToResponse(User user);

    UserRequest userEntityToRequest(User user);

}
