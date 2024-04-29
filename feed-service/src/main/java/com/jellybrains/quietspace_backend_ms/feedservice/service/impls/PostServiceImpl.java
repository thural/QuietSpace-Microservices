package com.jellybrains.quietspace_backend_ms.feedservice.service.impls;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.PostLikeMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.PostMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.model.PostLike;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostLikeRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.feedservice.utils.PagingProvider.buildCustomPageRequest;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final WebClient.Builder webClientBuilder;

    public final String AUTHOR_MISMATCH_MESSAGE = "post author mismatch with current user";
    private final PostLikeMapper postLikeMapper;

    private UUID getCurrentUserId(){

        return webClientBuilder.build().get()
                .uri("http://user-service/api/users/current-user")
                .retrieve()
                .bodyToMono(UUID.class)
                .block();
    };

    @Override
    public Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        return postRepository.findAll(pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public PostResponse addPost(PostRequest post) {
        UUID loggedUser = getCurrentUserId();
        Post postEntity = postMapper.postRequestToEntity(post);
        postEntity.setUserId(loggedUser);
        return postMapper.postEntityToResponse(postRepository.save(postEntity));
    }

    @Override
    public Optional<PostResponse> getPostById(UUID postId) {
        Post post = findPostEntityById(postId);
        PostResponse postResponse = postMapper.postEntityToResponse(post);
        return Optional.of(postResponse);
    }

    private Post findPostEntityById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void updatePost(UUID postId, PostRequest post) {
        UUID loggedUserId = getCurrentUserId();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUserId);
        if (postExistsByLoggedUser) {
            existingPost.setText(post.getTextContent());
            postRepository.save(existingPost);
        } // else throw new AccessDeniedException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public void patchPost(UUID postId, PostRequest post) {
        UUID loggedUserId = getCurrentUserId();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUserId);
        if (postExistsByLoggedUser) {
            if (StringUtils.hasText(post.getTextContent())) existingPost.setText(post.getTextContent());
            postRepository.save(existingPost);
        } // else throw new AccessDeniedException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public void deletePost(UUID postId) {
        UUID loggedUserId = getCurrentUserId();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUserId);
        if (postExistsByLoggedUser) postRepository.deleteById(postId);
//        else throw new AccessDeniedException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public Page<PostResponse> getPostsByUserId(UUID userId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        Page<Post> postPage;
        if (userId != null) {
            postPage = postRepository.findAllByUserId(userId, pageRequest);
        } else {
            postPage = postRepository.findAll(pageRequest);
        }
        return postPage.map(postMapper::postEntityToResponse);
    }

    private boolean isPostExistsByLoggedUser(Post existingPost, UUID loggedUserId) {
        return existingPost.getUserId().equals(loggedUserId);
    }

    @Override
    public List<PostLikeResponse> getPostLikesByPostId(UUID postId) {
        return postLikeRepository.findAllByPostId(postId)
                .stream().map(postLikeMapper::postLikeEntityToDto)
                .toList();
    }

    @Override
    public List<PostLikeResponse> getPostLikesByUserId(UUID userId) {
        return postLikeRepository.findAllByUserId(userId)
                .stream().map(postLikeMapper::postLikeEntityToDto)
                .toList();
    }

    @Override
    public void togglePostLike(UUID postId) {
        UUID loggedUserId = getCurrentUserId();
        boolean isPostLikeExists = postLikeRepository.existsByPostIdAndUserId(postId, loggedUserId);
        if (isPostLikeExists) postLikeRepository.deleteById(postId);
        else {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("post not found"));
            postLikeRepository.save(PostLike.builder().post(post).userId(loggedUserId).build());
        }
    }

}
