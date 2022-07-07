package com.sidepr.mono.sns.post.domain;

import com.sidepr.mono.sns.post.domain.id.PostLikeId;
import com.sidepr.mono.sns.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@IdClass(PostLikeId.class)
@NoArgsConstructor(access = PROTECTED)
public class PostLike {

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void likePost(Post post){
        if(Objects.nonNull(this.post)){
            this.post.getPostLikes()
                    .remove(this);
        }
        this.post = post;
    }
}
