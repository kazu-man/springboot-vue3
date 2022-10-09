package com.group.sampleproject.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.group.sampleproject.entity.Token;
import com.group.sampleproject.repository.TokenRepository;

@Service
public class TokenService {

    @Autowired
    TokenRepository tokenRepository;

    public String generateToken(String claim, Date tokenExTime){
     
        return JWT.create()
        .withExpiresAt(tokenExTime)
        .withIssuer("com.volkruss.toaru") //発行者
        .withClaim("username", claim) //keyに対してvalueの設定。汎用的な様々な値を保持できる
        .sign(Algorithm.HMAC256("secret")); // 利用アルゴリズムを指定してJWTを新規作成
    }

    public void createToken(Token token){
        tokenRepository.createToken(token);

    }

    public void deleteToken(Token token){
        tokenRepository.deleteToken(token);
    }

    public Token findByToken(String token){
        return tokenRepository.findByToken(token);
    }

    public Date getDatePlusMin(int min){

        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, min);
        return cal.getTime();
    }


    
    /** 
     * ユーザーネームから新しいtoken/refreshtokenを生成保存
     * @param username
     * @return Map<String, String>
     */
    public Map<String,String> createNewTokens(String username){

        // トークンの期限を取得
        Date tokenExTime = getDatePlusMin(1);
        Date refreshTokenExTime = getDatePlusMin(5);

        // // トークンの作成
        String token = generateToken(username, tokenExTime);
        // // リフレッシュトークンの作成
        String refreshToken = generateToken(username, refreshTokenExTime);
                
        //refreshToken　の保存
        Token newTokenEntity = new Token(token, refreshToken);
        createToken(newTokenEntity);    
        
        Map<String,String> result = new HashMap<>();
        result.put("token",token);
        result.put("refreshToken",refreshToken);
        return result;
            
    }
    
    
}
