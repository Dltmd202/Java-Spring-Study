package com.example.mongochat.user.exception;

import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.global.error.exception.BusinessException;

public class NotValidUserRelationException extends BusinessException {
    public NotValidUserRelationException(ErrorCode errorCode) {
        super(errorCode);
    }
}