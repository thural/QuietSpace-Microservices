package com.jellybrains.quietspace.common_service.model.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollResponse {

    private String id;
    private String votedOption;
    private Integer voteCount;
    private List<OptionResponse> options;
    private OffsetDateTime dueDate;

}