package com.sidepr.mono.sns.post.repository;

import com.sidepr.mono.sns.post.domain.PostLike;
import com.sidepr.mono.sns.post.domain.id.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
}
