package com.sidepr.mono.sns.post.domain;

import com.sidepr.mono.sns.comment.domain.Comment;
import com.sidepr.mono.sns.global.BaseTimeEntity;
import com.sidepr.mono.sns.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TINYINT default false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<PostTag> postTags = new ArrayList<>();

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.postComment(this);
    }

    public void addPostLike(PostLike postLike){
        this.postLikes.add(postLike);
        postLike.likePost(this);
    }

    public void deletePost(){
        this.isDeleted = false;
    }
}
