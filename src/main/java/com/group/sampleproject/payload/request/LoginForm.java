package com.group.sampleproject.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    private String username;
    private String password;
}