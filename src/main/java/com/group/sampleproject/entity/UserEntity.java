package com.group.sampleproject.entity;

import com.group.sampleproject.model.User;

import lombok.Getter;
import lombok.Setter;

/**
 * データアクセスのために使われるDTO
 */
@Getter
@Setter
public class UserEntity {
    private int id;
    private String name;
    private String password;
    private int coin;

    public User toUser(){
        return new User(name, coin);
    }
}
