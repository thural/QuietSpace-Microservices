package com.jellybrains.quietspace.user_service.mapper.custom;

import com.jellybrains.quietspace.common_service.model.request.CreateProfileRequest;
import com.jellybrains.quietspace.common_service.model.response.ProfileResponse;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.user_service.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    public Profile toEntity(CreateProfileRequest request){
        return Profile.builder()
                .dateOfBirth(request.getDateOfBirth())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .userId(request.getUserId())
                .username(request.getUsername())
                .email(request.getEmail())
                .build();
    }

    public UserResponse toUserResponse(Profile profile){
        return UserResponse.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .createDate(profile.getCreateDate())
                .updateDate(profile.getUpdateDate())
                .build();
    }

    public ProfileResponse toProfileResponse(Profile profile){
        ProfileResponse response = new ProfileResponse();
        BeanUtils.copyProperties(response, profile);
        return response;
    }

}
