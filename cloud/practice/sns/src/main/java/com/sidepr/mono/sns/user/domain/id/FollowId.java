package com.sidepr.mono.sns.user.domain.id;

import com.sidepr.mono.sns.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class FollowId implements Serializable {

    private User followed;

    private User follower;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId followId = (FollowId) o;
        return Objects.equals(followed, followId.followed) && Objects.equals(follower, followId.follower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followed, follower);
    }
}
