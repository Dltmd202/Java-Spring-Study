package com.sidepr.mono.sns.post.repository;

import com.sidepr.mono.sns.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
