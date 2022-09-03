package com.example.mongochat.user.domain.id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class FollowId implements Serializable {

    private Long followed;

    private Long follower;

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