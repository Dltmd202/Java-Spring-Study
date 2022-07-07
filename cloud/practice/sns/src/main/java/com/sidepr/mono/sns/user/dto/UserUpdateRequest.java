package com.sidepr.mono.sns.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateRequest {

    @Size(max = 12)
    @NotBlank(message = "닉네임을 입력해 주세요")
    private String nickname;

    private String description;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식을 지켜주세요")
    private String phoneNumber;

    private String profileImage;


    public void changeProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
}
