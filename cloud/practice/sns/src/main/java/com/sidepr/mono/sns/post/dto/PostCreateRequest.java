package com.sidepr.mono.sns.post.dto;

import com.sidepr.mono.sns.post.domain.Post;
import com.sidepr.mono.sns.user.domain.User;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class PostCreateRequest {

    private String content;

    public Post toEntity(User user){
        return Post.builder()
                .user(user)
                .content(this.content)
                .build();
    }
}
