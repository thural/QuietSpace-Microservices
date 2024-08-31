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
        Profile profile = new Profile();
        BeanUtils.copyProperties(request,profile);
        return profile;
    }

    public UserResponse toUserResponse(Profile profile){
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(profile, response);
        response.setId(profile.getUserId());
        return response;
    }

    public ProfileResponse toProfileResponse(Profile profile){
        ProfileResponse response = new ProfileResponse();
        BeanUtils.copyProperties(profile, response);
        return response;
    }

}
