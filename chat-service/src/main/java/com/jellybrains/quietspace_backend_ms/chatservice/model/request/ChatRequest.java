package com.jellybrains.quietspace_backend_ms.chatservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {

    @NotNull(message = "at least two members required to create the chat")
    private List<String> userIds;

}
