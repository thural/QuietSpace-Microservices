package com.jellybrains.quietspace_backend_ms.chatservice.model.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String id;
    private Integer version;
    private List<String> userIds;
    private List<UserResponse> members;
    private MessageResponse recentMessage;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
