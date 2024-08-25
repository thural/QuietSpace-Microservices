package com.jellybrains.quietspace.common_service.model.response;

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