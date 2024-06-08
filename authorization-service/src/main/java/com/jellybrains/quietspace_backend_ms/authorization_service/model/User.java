package com.jellybrains.quietspace_backend_ms.authorization_service.model;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String id;

    @Email
    private String email;

}
