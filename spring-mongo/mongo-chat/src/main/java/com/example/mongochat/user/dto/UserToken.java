package com.example.mongochat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserToken {

    private final Long id;
    private final String token;
    private final String roles;
}