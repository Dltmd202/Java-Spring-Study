package com.sidepr.mono.sns.user.exception;


import com.sidepr.mono.sns.global.error.ErrorCode;
import com.sidepr.mono.sns.global.error.exception.NotFoundException;

public class NotFoundUserException extends NotFoundException {

    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
