package com.example.mongochat.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND_RESOURCE_ERROR(
            "존재하지 않는 리소스입니다.",
            HttpStatus.BAD_REQUEST.value()
    ),
    INTERNAL_SERVER_ERROR(
            "서버 내부 에러입니다.",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
    ),
    INVALID_INPUT_ERROR(
            "적절한 입력값이 아닙니다.",
            HttpStatus.BAD_REQUEST.value()
    ),
    CONFLICT_VALUE_ERROR(
            "중복된 값입니다",
            HttpStatus.CONFLICT.value()
    ),
    CONFLICT_PASSWORD_ERROR(
            "잘못된 비밀번호입니다.",
            HttpStatus.CONFLICT.value()
    ),
    NOT_PERMITTED_RESOURCE_ERROR(
            "접근할 수 없는 리소스입니다.",
            HttpStatus.UNAUTHORIZED.value()
    ),

    NOT_VALID_REQUEST_ERROR(
            "잘못된 요청입니다.",
            HttpStatus.BAD_REQUEST.value()
    );


    private final String message;
    private final int status;

    ErrorCode(String message, int status){
        this.message = message;
        this.status = status;
    }
}