package com.sidepr.mono.sns.comment.domain;

import com.sidepr.mono.sns.comment.domain.id.CommentLikeUserId;
import com.sidepr.mono.sns.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@IdClass(CommentLikeUserId.class)
@NoArgsConstructor(access = PROTECTED)
public class CommentLike {

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void likeComment(Comment comment) {
        if(Objects.nonNull(this.comment)){
            this.comment.getCommentLikes()
                    .remove(this);
        }
        this.comment = comment;
    }
}
