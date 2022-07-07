package com.sidepr.mono.sns.user.dto;


import com.sidepr.mono.sns.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private String nickname;
    private String profileImage;

    @Builder
    public UserResponse(
            Long userId, String name, String email, String nickname, String profileImage
    ) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static UserResponse of(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage()
        );
    }
}
