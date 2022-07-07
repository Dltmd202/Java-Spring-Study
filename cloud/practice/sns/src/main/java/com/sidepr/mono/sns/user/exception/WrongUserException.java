package com.sidepr.mono.sns.user.exception;

import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.global.error.exception.BusinessException;

public class WrongUserException extends BusinessException{
    public WrongUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
