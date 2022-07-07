package com.sidepr.mono.sns.user.domain;

import com.sidepr.mono.sns.bookmark.domain.Bookmark;
import com.sidepr.mono.sns.user.exception.NotSamePasswordException;
import com.sidepr.mono.sns.global.BaseTimeEntity;
import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.user.dto.UserPasswordChangeRequest;
import com.sidepr.mono.sns.user.dto.UserUpdateRequest;
import com.sidepr.mono.sns.user.security.Jwt;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

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

    @Column(length = 30)
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

    @OneToMany(mappedBy = "followed", cascade = ALL)
    private List<Follow> follower = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = ALL)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "TINYINT default false")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private Role roles;

    public void updateUserInfo(UserUpdateRequest userUpdateRequest){
        this.nickname = userUpdateRequest.getNickname();
        this.description = userUpdateRequest.getDescription();
        this.phoneNumber = userUpdateRequest.getPhoneNumber();
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
}