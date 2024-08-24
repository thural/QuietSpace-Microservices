package com.jellybrains.quietspace_backend_ms.notification_service.common.model.response;

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