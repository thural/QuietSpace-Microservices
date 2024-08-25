package com.jellybrains.quietspace_backend_ms.userservice.common.model.response;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponse {

    private String id;
    private String label;
    private String voteShare;

}