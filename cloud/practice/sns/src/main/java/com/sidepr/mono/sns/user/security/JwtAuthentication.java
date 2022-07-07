package com.sidepr.mono.sns.user.security;

import lombok.Getter;

@Getter
public class JwtAuthentication {

    private final Long id;

    private final String token;

    JwtAuthentication(Long id, String token) {

        this.id = id;
        this.token = token;
    }

}