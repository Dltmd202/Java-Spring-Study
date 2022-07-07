package com.sidepr.mono.sns.post.repository;

import com.sidepr.mono.sns.post.domain.PostTag;
import com.sidepr.mono.sns.post.domain.id.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
}
