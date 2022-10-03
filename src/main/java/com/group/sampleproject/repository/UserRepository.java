package com.group.sampleproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.group.sampleproject.entity.UserEntity;

@Mapper
public interface UserRepository {
    UserEntity findByName(String name);
}
