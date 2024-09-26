package com.jellybrains.quietspace.feed_service.mapper.custom;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.model.request.PollRequest;
import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.response.OptionResponse;
import com.jellybrains.quietspace.common_service.model.response.PollResponse;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;
import com.jellybrains.quietspace.common_service.service.shared.ReactionService;
import com.jellybrains.quietspace.common_service.service.shared.UserService;
import com.jellybrains.quietspace.feed_service.entity.Poll;
import com.jellybrains.quietspace.feed_service.entity.PollOption;
import com.jellybrains.quietspace.feed_service.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final ReactionService reactionService;
    private final UserService userService;

    public Post postRequestToEntity(PostRequest postRequest) {
        Post post = Post.builder()
                .userId(getLoggedUserId())
                .title(postRequest.getTitle())
                .text(postRequest.getText())
                .build();

        if (postRequest.getPoll() == null) return post;

        PollRequest pollRequest = postRequest.getPoll();

        Poll newPoll = Poll.builder()
                .post(post)
                .dueDate(pollRequest.getDueDate())
                .build();

        List<PollOption> options = pollRequest.getOptions().stream()
                .<PollOption>map(option -> PollOption.builder()
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
        Integer commentCount = post.getComments() != null ? post.getComments().size() : 0;
        Integer likeCount = reactionService.getLikeCount(post.getId());
        Integer dislikeCount = reactionService.getDislikeCount(post.getId());
        String username = userService.getUsernameById(post.getUserId());
        ReactionResponse userReaction = reactionService.getUserReactionByContentId(post.getId(), ContentType.POST);

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .commentCount(commentCount)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .userId(post.getUserId())
                .username(username)
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
                .votedOption(getVotedPollOptionLabel(post.getPoll(), post.getUserId()))
                .voteCount(getVoteCount(post.getPoll()))
                .build();

        postResponse.setPoll(pollResponse);
        return postResponse;
    }

    private Integer getVoteCount(Poll poll) {
        return poll.getOptions().stream()
                .map(option -> option.getVotes().size())
                .reduce(0, Integer::sum);
    }

    private String getVoteShare(PollOption option) {
        Integer totalVoteCount = getVoteCount(option.getPoll());
        int optionVoteNum = option.getVotes() != null ? option.getVotes().size() : 0;
        if (totalVoteCount < 1) return "0%";
        return (optionVoteNum * 100 / totalVoteCount) + "%";
    }

    private String getVotedPollOptionLabel(Poll poll, String userId) {
        return poll.getOptions().stream()
                .filter(option -> option.getVotes().contains(userId))
                .findFirst()
                .map(PollOption::getLabel)
                .orElse("not voted");
    }

    private String getLoggedUserId() {
        return userService.getAuthorizedUserId();
    }

}
