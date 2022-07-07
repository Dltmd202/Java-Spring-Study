package com.sidepr.mono.sns.user.domain;


import com.sidepr.mono.sns.global.BaseTimeEntity;
import com.sidepr.mono.sns.user.domain.id.FollowId;
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
    @JoinColumn(name = "followed")
    private User followed;

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower")
    private User follower;

}
