package com.example.mongochat.global.error.exception;

import com.sidepr.mono.sns.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class RuntimeIOException extends RuntimeException{

    private final ErrorCode errorCode;

    public RuntimeIOException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}