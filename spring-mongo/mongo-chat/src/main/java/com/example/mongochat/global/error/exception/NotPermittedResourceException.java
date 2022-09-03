package com.example.mongochat.global.error.exception;

import com.sidepr.mono.sns.global.error.ErrorCode;

public class NotPermittedResourceException extends BusinessException{
    public NotPermittedResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}