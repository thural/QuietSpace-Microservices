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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAllPaged(pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public Mono<PostResponse> addPost(PostRequest post) {
        post.setUserId(userService.getAuthorizedUserId());
        return postRepository.save(postMapper.postRequestToEntity(post))
                .map(postMapper::postEntityToResponse);
    }

    @Override
    public Mono<PostResponse> getPostById(String postId) {
        Mono<Post> post = findPostEntityById(postId);
        return post.map(postMapper::postEntityToResponse);
    }

    @Override
    public Mono<PostResponse> patchPost(PostRequest post) {
        Mono<Post> existingPost = findPostEntityById(post.getPostId());
        validateResourceOwnership(existingPost);
        BeanUtils.copyProperties(post, existingPost);
        return existingPost.map(postMapper::postEntityToResponse);
    }

    @Override
    public Mono<Void> votePoll(VoteRequest voteRequest) {
        return postRepository.findById(voteRequest.getPostId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException()))
                .doOnNext(post -> {
                    if (post.getPoll().getOptions().stream()
                            .anyMatch(option -> option.getVotes()
                                    .contains(voteRequest.getUserId())))
                        throw new CustomErrorException("already voted");
                })
                .doOnNext(post -> {
                    post.getPoll().getOptions().stream()
                            .filter(option -> option.getLabel().equals(voteRequest.getOption()))
                            .findFirst().ifPresent(option -> {
                                Set<String> votes = option.getVotes();
                                votes.add(voteRequest.getUserId());
                                option.setVotes(votes);
                            });
                }).then();
    }

    @Override
    public Mono<Void> deletePost(String postId) {
        Mono<Post> existingPost = findPostEntityById(postId);
        validateResourceOwnership(existingPost);
        return postRepository.deleteById(postId).then();
    }

    @Override
    public Flux<PostResponse> getPostsByUserId(String userId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.findAllByUserId(userId, pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public Flux<PostResponse> getAllByQuery(String query, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return postRepository.searchByTitleOrText(query, pageRequest).map(postMapper::postEntityToResponse);
    }

    private void validateResourceOwnership(Mono<Post> existingPost) {
        String loggedUserId = userService.getAuthorizedUserId();
        existingPost.doOnNext(post -> {
            if (!post.getUserId().equals(loggedUserId))
                throw new CustomErrorException("post author mismatch with current user");
        });
    }

    private Mono<Post> findPostEntityById(String postId) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(EntityNotFoundException::new));
    }

}
