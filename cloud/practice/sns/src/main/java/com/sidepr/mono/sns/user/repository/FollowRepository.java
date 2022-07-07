package com.sidepr.mono.sns.user.repository;



import com.sidepr.mono.sns.user.domain.Follow;
import com.sidepr.mono.sns.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("select f from User u left join Follow f where u = :username")
    public Page<User> findFriend(@Param("username") String username, Pageable pageable);
}
