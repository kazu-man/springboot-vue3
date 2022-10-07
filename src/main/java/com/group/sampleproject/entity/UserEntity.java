package com.group.sampleproject.entity;

import java.util.List;

import com.group.sampleproject.model.LoginUserModel;
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
    private String username;
    private String password;
    private String email;
    private String role;
    private List<AttendanceEntity> attendanceList;

    public User toUser(){
        return new User(username);
    }

    public LoginUserModel toLoginUserModel(){
        return new LoginUserModel(username,email,role);
    }
}
