package com.sidepr.mono.sns.comment.domain.id;

import com.sidepr.mono.sns.comment.domain.Comment;
import com.sidepr.mono.sns.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class CommentTagUserId implements Serializable {

    private Comment comment;

    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentTagUserId that = (CommentTagUserId) o;
        return Objects.equals(comment, that.comment) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, user);
    }
}
