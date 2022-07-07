package com.sidepr.mono.sns.global.error.exception;


import com.sidepr.mono.sns.global.error.ErrorCode;

public class NotFoundException extends BusinessException {


    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}