package com.jellybrains.quietspace.feed_service.service.impls;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.feed_service.entity.Poll;
import com.jellybrains.quietspace.feed_service.entity.PollOption;
import com.jellybrains.quietspace.feed_service.entity.Post;
import com.jellybrains.quietspace.feed_service.exception.CustomErrorException;
import com.jellybrains.quietspace.feed_service.mapper.custom.PostMapper;
import com.jellybrains.quietspace.feed_service.repository.PostRepository;
import com.jellybrains.quietspace.feed_service.service.PostService;
import com.jellybrains.quietspace.feed_service.webclient.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostMapper postMapper;

    public final String AUTHOR_MISMATCH_MESSAGE = "post author mismatch with current user";

    @Override
    public Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAll(pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public PostResponse addPost(PostRequest post) {
        String loggedUserId = userService.getAuthorizedUserId();
        log.info("signed USER ID during posting to feed: {}", loggedUserId);
        if (!loggedUserId.equals(post.getUserId()))
            throw new CustomErrorException(AUTHOR_MISMATCH_MESSAGE);
        return postMapper.postEntityToResponse(
                postRepository.save(postMapper.postRequestToEntity(post))
        );
    }

    public String getVotedPollOptionLabel(Poll poll) {
        String userId = userService.getAuthorizedUserId();

        return poll.getOptions().stream()
                .filter(option -> option.getVotes().contains(userId))
                .findAny()
                .map(PollOption::getLabel).orElse("not voted");
    }

    @Override
    public Optional<PostResponse> getPostById(String postId) {
        Post post = findPostEntityById(postId);
        return Optional.of(postMapper.postEntityToResponse(post));
    }

    @Override
    public PostResponse updatePost(String postId, PostRequest post) {
        String loggedUserId = userService.getAuthorizedUserId();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUserId);
        if (postExistsByLoggedUser) {
            existingPost.setText(post.getText());
            return postMapper.postEntityToResponse(postRepository.save(existingPost));
        } else throw new CustomErrorException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public PostResponse patchPost(String postId, PostRequest post) {
        String loggedUserId = userService.getAuthorizedUserId();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUserId);
        if (postExistsByLoggedUser) {
            if (StringUtils.hasText(post.getText())) existingPost.setText(post.getText());
            return postMapper.postEntityToResponse(postRepository.save(existingPost));
        } else throw new CustomErrorException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public void votePoll(VoteRequest voteRequest) {
        Post foundPost = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(EntityNotFoundException::new);

        if (foundPost.getPoll().getOptions().stream()
                .anyMatch(option -> option.getVotes().contains(voteRequest.getUserId()))) return;

        foundPost.getPoll().getOptions().stream()
                .filter(option -> option.getLabel().equals(voteRequest.getOption()))
                .findFirst()
                .ifPresent(option -> {
                    Set<String> votes = option.getVotes();
                    votes.add(voteRequest.getUserId());
                    option.setVotes(votes);
                });

        postRepository.save(foundPost);
    }

    @Override
    public void deletePost(String postId) {
        String loggedUserId = userService.getAuthorizedUserId();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUserId);
        if (postExistsByLoggedUser) postRepository.deleteById(postId);
        else throw new CustomErrorException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public Page<PostResponse> getPostsByUserId(String userId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        Page<Post> postPage;
        if (userId != null) {
            postPage = postRepository.findAllByUserId(userId, pageRequest);
        } else {
            postPage = postRepository.findAll(pageRequest);
        }
        return postPage.map(postMapper::postEntityToResponse);
    }

    @Override
    public Page<PostResponse> getAllByQuery(String query, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAllByQuery(query, pageRequest).map(postMapper::postEntityToResponse);
    }

    private boolean isPostExistsByLoggedUser(Post existingPost, String loggedUserId) {
        return existingPost.getUserId().equals(loggedUserId);
    }

    private Post findPostEntityById(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
    }

}
