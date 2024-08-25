package com.jellybrains.quietspace.common_service.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class CreateProfileRequest {

    @NotNull(message = "user id is required for profile creation")
    private String userId;

    @NotEmpty(message = "username field is empty")
    @NotNull(message = "username is required")
    private String username;

    @Email(message = "invalid email format")
    @NotEmpty(message = "email field is empty")
    @NotNull(message = "email is required")
    private String email;

    private String firstname;

    private String lastname;

    private OffsetDateTime dateOfBirth;

}
