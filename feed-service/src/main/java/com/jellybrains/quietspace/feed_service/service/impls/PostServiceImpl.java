package com.jellybrains.quietspace.feed_service.service.impls;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import com.jellybrains.quietspace.feed_service.entity.Post;
import com.jellybrains.quietspace.feed_service.exception.CustomErrorException;
import com.jellybrains.quietspace.feed_service.mapper.custom.PostMapper;
import com.jellybrains.quietspace.feed_service.repository.PostRepository;
import com.jellybrains.quietspace.feed_service.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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


    @Override
    public Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAll(pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public PostResponse addPost(PostRequest post) {
        post.setUserId(userService.getAuthorizedUserId());
        return postMapper.postEntityToResponse(
                postRepository.save(postMapper.postRequestToEntity(post))
        );
    }

    @Override
    public Optional<PostResponse> getPostById(String postId) {
        Post post = findPostEntityById(postId);
        return Optional.of(postMapper.postEntityToResponse(post));
    }

    @Override
    public PostResponse patchPost(PostRequest post) {
        Post existingPost = findPostEntityById(post.getPostId());
        validateResourceOwnership(existingPost);
        BeanUtils.copyProperties(post, existingPost);
        return postMapper.postEntityToResponse(postRepository.save(existingPost));
    }

    @Override
    public void votePoll(VoteRequest voteRequest) {
        Post foundPost = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(EntityNotFoundException::new);
        if (foundPost.getPoll().getOptions().stream().anyMatch(option -> option.getVotes()
                .contains(voteRequest.getUserId()))) throw new CustomErrorException("already voted");
        foundPost.getPoll().getOptions().stream()
                .filter(option -> option.getLabel().equals(voteRequest.getOption()))
                .findFirst().ifPresent(option -> {
                    Set<String> votes = option.getVotes();
                    votes.add(voteRequest.getUserId());
                    option.setVotes(votes);
                });
        postRepository.save(foundPost);
    }

    @Override
    public void deletePost(String postId) {
        Post existingPost = findPostEntityById(postId);
        validateResourceOwnership(existingPost);
        postRepository.deleteById(postId);
    }

    @Override
    public Page<PostResponse> getPostsByUserId(String userId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAllByUserId(userId, pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public Page<PostResponse> getAllByQuery(String query, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAllByQuery(query, pageRequest).map(postMapper::postEntityToResponse);
    }

    private void validateResourceOwnership(Post existingPost) {
        String loggedUserId = userService.getAuthorizedUserId();
        if (!existingPost.getUserId().equals(loggedUserId))
            throw new CustomErrorException("post author mismatch with current user");
    }

    private Post findPostEntityById(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
    }

}
