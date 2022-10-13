package com.group.sampleproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.group.sampleproject.entity.UserEntity;

@Mapper
public interface UserRepository {
    UserEntity findByName(String username);
    UserEntity existsByUsername(String username);
    UserEntity existsByEmail(String email);
    void save(UserEntity user);
    
}
