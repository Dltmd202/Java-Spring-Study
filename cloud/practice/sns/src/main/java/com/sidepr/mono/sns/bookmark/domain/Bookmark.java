package com.sidepr.mono.sns.bookmark.domain;

import com.sidepr.mono.sns.bookmark.domain.id.BookmarkId;
import com.sidepr.mono.sns.post.domain.Post;
import com.sidepr.mono.sns.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@IdClass(BookmarkId.class)
@NoArgsConstructor(access = PROTECTED)
public class Bookmark {

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
