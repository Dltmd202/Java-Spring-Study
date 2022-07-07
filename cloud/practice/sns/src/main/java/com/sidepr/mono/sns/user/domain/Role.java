package com.sidepr.mono.sns.user.domain;

import lombok.Getter;

@Getter
public enum Role {

    USER("ROLE_USER"),

    ADMIN("ROLE_ADMIN");

    private final String value;

    Role(String value){
        this.value = value;
    }

    public static Role of(String name){
        for (Role role : Role.values()) {
            if(role.name().equalsIgnoreCase(name)){
                return role;
            }
        }
        return null;
    }
}
