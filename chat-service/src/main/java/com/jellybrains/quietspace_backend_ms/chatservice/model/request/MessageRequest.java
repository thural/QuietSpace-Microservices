package com.jellybrains.quietspace_backend_ms.chatservice.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @NotNull(message = "chat id can not be null")
    private String chatId;

    @NotNull(message = "sender id can not be null")
    private String senderId;

    @NotNull
    private String recipientId;

    @NotNull(message = "text content can not be null")
    @NotBlank(message = "text content can not be blank")
    @Size(min = 1, max = 1000, message = "at lest 1 and max 1000 characters expected")
    private String text;

}
