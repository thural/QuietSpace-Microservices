package com.jellybrains.quietspace_backend_ms.feedservice.service.impls;

import com.jellybrains.quietspace_backend_ms.feedservice.entity.Poll;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.PollOption;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.custom.PostMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.VoteRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.PostResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.feedservice.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.feedservice.utils.PagingProvider.buildPageRequest;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserClient userClient;
    private final PostMapper postMapper;

    public final String AUTHOR_MISMATCH_MESSAGE = "post author mismatch with current user";

    @Override
    public Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAll(pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public PostResponse addPost(PostRequest post) {
        return postMapper.postEntityToResponse(
                postRepository.save(postMapper.postRequestToEntity(post))
        );
    }

    public String getVotedPollOptionLabel(Poll poll){
        UUID userId = userClient.getLoggedUser()
                .map(UserResponse::getId)
                .orElseThrow(UserNotFoundException::new);

        return poll.getOptions().stream()
               .filter(option -> option.getVotes().contains(userId))
               .findAny()
                .map(PollOption::getLabel).orElse("not voted");
    }

    @Override
    public Optional<PostResponse> getPostById(UUID postId) {
        Post post = findPostEntityById(postId);
        return Optional.of(postMapper.postEntityToResponse(post));
    }

    @Override
    public PostResponse updatePost(UUID postId, PostRequest post) {
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost);
        if (postExistsByLoggedUser) {
            existingPost.setText(post.getText());
            return postMapper.postEntityToResponse(postRepository.save(existingPost));
        } else throw new BadRequestException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public PostResponse patchPost(UUID postId, PostRequest post) {
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost);
        if (postExistsByLoggedUser) {
            if (StringUtils.hasText(post.getText())) existingPost.setText(post.getText());
            return postMapper.postEntityToResponse(postRepository.save(existingPost));
        } else throw new BadRequestException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public void votePoll(VoteRequest voteRequest) {
        Post foundPost = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(EntityNotFoundException::new);

        if(foundPost.getPoll().getOptions().stream()
                .anyMatch(option -> option.getVotes().contains(voteRequest.getUserId()))) return;

        foundPost.getPoll().getOptions().stream()
                .filter(option -> option.getLabel().equals(voteRequest.getOption()))
                .findFirst()
                .ifPresent(option -> {
                    Set<UUID> votes = option.getVotes();
                    votes.add(voteRequest.getUserId());
                    option.setVotes(votes);
                });

        postRepository.save(foundPost);
    }

    @Override
    public void deletePost(UUID postId) {
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost);
        if (postExistsByLoggedUser) postRepository.deleteById(postId);
        else throw new BadRequestException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public Page<PostResponse> getPostsByUserId(UUID userId, Integer pageNumber, Integer pageSize) {
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

    private boolean isPostExistsByLoggedUser(Post existingPost) {
        return existingPost.getUserId().equals(userClient.getLoggedUser()
                .map(UserResponse::getId)
                .orElseThrow(UserNotFoundException::new));
    }

    private Post findPostEntityById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
    }

}
