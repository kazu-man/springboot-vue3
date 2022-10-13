package com.group.sampleproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.model.LoginUserModel;
import com.group.sampleproject.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;


    public LoginUserModel getLoginUserByUsername(String username){

        UserEntity user = userRepository.findByName(username);
        return user.toLoginUserModel();

    }
}


