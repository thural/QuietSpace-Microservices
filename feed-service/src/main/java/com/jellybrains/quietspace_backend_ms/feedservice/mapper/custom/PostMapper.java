package com.jellybrains.quietspace_backend_ms.feedservice.mapper.custom;

import com.jellybrains.quietspace_backend_ms.feedservice.entity.Poll;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.PollOption;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.PollRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostMapper {

    public Post postRequestToEntity(PostRequest postRequest) {
        Post post = Post.builder()
                .userId(getLoggedUser().getId())
                .title(postRequest.getTitle())
                .text(postRequest.getText())
                .build();

        if(postRequest.getPoll() == null) return post;

        PollRequest pollRequest = postRequest.getPoll();

        Poll newPoll = Poll.builder()
                .post(post)
                .dueDate(pollRequest.getDueDate())
                .build();

        List<PollOption> options = pollRequest.getOptions().stream()
                .map(option -> PollOption.builder()
                        .label(option)
                        .poll(newPoll)
                        .votes(new HashSet<>())
                        .build())
                .toList();

        newPoll.setOptions(options);
        post.setPoll(newPoll);

        return post;
    }

   public PostResponse postEntityToResponse(Post post) {
        Integer commentCount = post.getComments().size();
        Integer postLikeCount = getLikeCountByContentId(post.getId());
        Integer dislikeCount = getDislikeCountByContentId(post.getId());
        ReactionResponse userReaction = getUserReactionByContentId(post.getId())
                .orElse(null);

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .commentCount(commentCount)
                .likeCount(postLikeCount)
                .dislikeCount(dislikeCount)
                .userId(post.getUserId().getId())
                .username(post.getUserId().getUsername())
                .userReaction(userReaction)
                .createDate(post.getCreateDate())
                .updateDate(post.getUpdateDate())
                .build();

        if (post.getPoll() == null) return postResponse;

        List<OptionResponse> options = post.getPoll().getOptions().stream()
                .map(option -> OptionResponse.builder()
                        .id(option.getId())
                        .label(option.getLabel())
                        .voteShare(getVoteShare(option))
                        .build())
                .toList();

        PollResponse pollResponse = PollResponse.builder()
                .id(post.getPoll().getId())
                .options(options)
                .votedOption(getVotedPollOptionLabel(post.getPoll(), post.getUserId().getId()))
                .voteCount(getVoteCount(post.getPoll()))
                .build();

        postResponse.setPoll(pollResponse);
        return postResponse;
    }

    private Integer getVoteCount(Poll poll){
        return poll.getOptions().stream()
                .map(option -> option.getVotes().size())
                .reduce(0, Integer::sum);
    }

    private String getVoteShare(PollOption option){
        Integer totalVoteCount = getVoteCount(option.getPoll());
        int optionVoteNum = option.getVotes().size();
        if (totalVoteCount < 1) return "0%";
        return (optionVoteNum * 100/totalVoteCount) +  "%";
    }

    private String getVotedPollOptionLabel(Poll poll, UUID userId){
        return poll.getOptions().stream()
                .filter(option -> option.getVotes().contains(userId))
                .findFirst()
                .map(PollOption::getLabel)
                .orElse("not voted");
    }

    private UserResponse getLoggedUser(){
        return null; // TODO: get logged user using webclient
    }

    private Integer getLikeCountByContentId(UUID contentId){
        return null; // TODO: get like count using webclient
    }

    private Integer getDislikeCountByContentId(UUID contentId){
        return null; // TODO: get dislike count using webclient
    }

    ReactionResponse getUserReactionByContentId(UUID contentId){
        return null; // TODO: get user reaction using webclient
    }


}
