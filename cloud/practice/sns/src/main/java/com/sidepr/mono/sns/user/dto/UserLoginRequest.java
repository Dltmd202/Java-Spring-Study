package com.sidepr.mono.sns.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@Getter
public class UserLoginRequest {

    @NotBlank(message = "이메일을 입력해 주세요")
    @Email(message = "올바른 이메일 주소를 입력해 주세요")
    private String principal;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상입니다.")
    private String credentials;


    @Override
    public String toString() {
        return "UserLoginRequest{" + "principal='" + principal + '\'' + ", credentials='" + credentials + '\'' + '}';
    }
}

