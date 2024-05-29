package com.jellybrains.quietspace_backend_ms.chatservice.model.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponse {

    private UUID id;
    private String label;
    private String voteShare;

}