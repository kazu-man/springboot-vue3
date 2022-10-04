package com.group.sampleproject.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    private int id;
    private String token;
    private String refreshToken;

    public Token(String token, String refreshToken){
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
