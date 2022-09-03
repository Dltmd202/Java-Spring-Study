package com.example.mongochat.user.domain;


import com.sidepr.mono.sns.global.BaseTimeEntity;
import com.sidepr.mono.sns.user.domain.id.FollowId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@IdClass(FollowId.class)
@NoArgsConstructor(access = PROTECTED)
public class Follow extends BaseTimeEntity {

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "followed", nullable = false)
    private User followed;

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower", nullable = false)
    private User follower;

    @Builder
    public Follow(User followed, User following) {
        this.follower = followed;
        this.followed = following;
    }
}