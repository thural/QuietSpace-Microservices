package com.jellybrains.quietspace_backend_ms.userservice.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotNull(message = "user role can not be null")
    @NotBlank(message = "user role can not be blank")
    @Size(min = 1, max = 16, message = "1 to 16 characters are expected for user role")
    private String role;

    @NotNull(message = "username can not be null")
    @NotBlank(message = "username can not be blank")
    @Size(min = 1, max = 32, message = "at least 1 and max 32 characters expected")
    private String username;

    @Email(message = "email is not in valid format")
    @NotNull(message = "email can not be null")
    @NotBlank(message = "email can not be blank")
    @Size(max = 32, message = "max 32 characters expected for email")
    private String email;

    @NotNull(message = "password can not be null")
    @NotBlank(message = "password can nto be blank")
    @Size(min = 8, max = 32, message = "at least 8 and max 32 characters expected for password")
    private String password;

}