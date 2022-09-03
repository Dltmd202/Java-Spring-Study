package com.example.mongochat.user.exception;


import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.global.error.exception.BusinessException;

public class DuplicateUserException extends BusinessException {
    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}