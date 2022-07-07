package com.sidepr.mono.sns.post.domain;

import com.sidepr.mono.sns.post.domain.id.PostTagId;
import com.sidepr.mono.sns.tag.domain.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@IdClass(PostTagId.class)
@NoArgsConstructor(access = PROTECTED)
public class PostTag {

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Id @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
