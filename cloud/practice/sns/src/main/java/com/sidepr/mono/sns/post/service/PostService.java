package com.sidepr.mono.sns.post.service;

import com.sidepr.mono.sns.post.domain.Post;
import com.sidepr.mono.sns.post.dto.PostCreateRequest;
import com.sidepr.mono.sns.post.exception.NotFoundPostException;
import com.sidepr.mono.sns.post.repository.PostRepository;
import com.sidepr.mono.sns.user.domain.User;
import com.sidepr.mono.sns.user.exception.NotFoundUserException;
import com.sidepr.mono.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sidepr.mono.sns.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long savePost(
            Long userId, PostCreateRequest postCreateRequest
    ){
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
        Post post = postCreateRequest.toEntity(user);

        return postRepository.save(post).getId();
    }

    public Long deletePost(Long postId){
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundPostException(NOT_FOUND_RESOURCE_ERROR));
        post.deletePost();
        return post.getId();
    }
}
