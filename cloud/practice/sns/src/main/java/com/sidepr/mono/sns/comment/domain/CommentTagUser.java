package com.sidepr.mono.sns.comment.domain;

import com.sidepr.mono.sns.comment.domain.id.CommentTagUserId;
import com.sidepr.mono.sns.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@IdClass(CommentTagUserId.class)
@NoArgsConstructor(access = PROTECTED)
public class CommentTagUser {

    @Id @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Id @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
