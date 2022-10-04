package com.group.sampleproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.group.sampleproject.entity.Token;

@Mapper
public interface TokenRepository {
    Token findByToken(String token);
    void deleteToken(Token token);
    void createToken(Token token);
}
