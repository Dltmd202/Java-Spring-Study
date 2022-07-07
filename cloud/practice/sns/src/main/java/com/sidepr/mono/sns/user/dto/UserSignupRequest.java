package com.sidepr.mono.sns.user.dto;


import com.sidepr.mono.sns.user.exception.NotValidPasswordException;
import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.user.domain.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignupRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이베일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 숫자,영문,특수문자를 조합해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String checkPassword;

    @Size(max = 12)
    @NotBlank(message = "닉네임을 입력해 주세요")
    private String nickname;

    @Size(max = 15)
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }

    public boolean isDifferentPassword(){
        if(!this.password.equals(this.checkPassword)){
            throw new NotValidPasswordException(ErrorCode.CONFLICT_VALUE_ERROR);
        }
        return false;
    }

    public void setEncodedPassword(String password){
        this.password = password;
    }

}
