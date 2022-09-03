package com.example.mongochat.user.domain;

import com.sidepr.mono.sns.bookmark.domain.Bookmark;
import com.sidepr.mono.sns.global.BaseTimeEntity;
import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.user.dto.UserPasswordChangeRequest;
import com.sidepr.mono.sns.user.dto.UserUpdateRequest;
import com.sidepr.mono.sns.user.exception.NotSamePasswordException;
import com.sidepr.mono.sns.user.security.Jwt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(
            length = 30,
            unique = true,
            nullable = false

    )
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String phoneNumber;

    @Builder.Default
    @OneToMany(mappedBy = "followed", cascade = ALL, orphanRemoval = true)
    private List<Follow> follower = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "follower", cascade = ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "TINYINT default false")
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private Role roles = Role.USER;

    @Builder
    public User(Long id, String name, String nickname, String email, String password, String profileImage, String description, String phoneNumber, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.isDeleted = isDeleted;
        this.roles = Role.USER;
    }

    public User(String name, String nickname, String email, String password, String profileImage, String description, String phoneNumber) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    public User(String name, String nickname, String email, String password, String profileImage, String description, String phoneNumber, Role role) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.roles = role;
    }

    public void updateUserInfo(UserUpdateRequest userUpdateRequest){
        this.nickname = userUpdateRequest.getNickname();
        this.description = userUpdateRequest.getDescription();
        this.phoneNumber = userUpdateRequest.getPhoneNumber();
        this.profileImage = userUpdateRequest.getProfileImage();
    }

    public void login(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, password)) {
            throw new IllegalArgumentException("Bad credential");
        }
    }

    public String newJwt(Jwt jwt, String[] roles){
        Jwt.Claims claims = Jwt.Claims.of(id, name, roles);
        return jwt.create(claims);
    }

    public void updateUserDeleted() {
        this.isDeleted = true;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if(!passwordEncoder.matches(
                credentials,
                password
        )){
            throw new NotSamePasswordException(ErrorCode.CONFLICT_PASSWORD_ERROR);
        }
    }

    public void updateUserPasswordInfo(PasswordEncoder passwordEncoder, UserPasswordChangeRequest userPasswordChangeRequest) {
        this.password = passwordEncoder.encode(userPasswordChangeRequest.getNewPassword());
    }

    public void followUser(Follow follow){
        this.getFollowing().add(follow);
        follow.getFollowed().getFollower().add(follow);
    }
}