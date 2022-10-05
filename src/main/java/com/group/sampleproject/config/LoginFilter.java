package com.group.sampleproject.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.group.sampleproject.entity.Token;
import com.group.sampleproject.service.TokenService;

public class LoginFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    public LoginFilter(TokenService tokenService){
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // headerからTokenを取得する
        String authHeader = request.getHeader("X-AUTH-TOKEN");

        //　チェック処理
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = authHeader.substring(7);

        // Tokenの検証と認証を行う
        String username = JWT.decode(token).getClaim("username").asString();
        DecodedJWT decodedJWT = null;

        try{
            decodedJWT = JWT.require(Algorithm.HMAC256("secret")).build().verify(token);

        }catch(JWTVerificationException e){

        }


        //認証失敗した際はrefreshTokenを検証
        if(decodedJWT == null){

            //tokenでrefreshTokenを検索
            Token tokenEntitity = tokenService.findByToken(token);
            String refreshToken = tokenEntitity.getRefreshToken();

            //refreshTokenの検証
            decodedJWT = JWT.require(Algorithm.HMAC256("secret")).build().verify(refreshToken);

            Calendar cal = Calendar.getInstance();

            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, 1);
            Date tokenExTime = cal.getTime();
            cal.add(Calendar.DATE, 1);
            Date refreshTokenExTime = cal.getTime();
            
            // // トークンの作成
            String newToken = tokenService.generateToken(username, tokenExTime);
            // // リフレッシュトークンの作成
            refreshToken = tokenService.generateToken(username, refreshTokenExTime);

            //古いtokenを削除し、新しいrefreshTokenを保存
            tokenService.deleteToken(tokenEntitity);
            Token newTokenEntity = new Token(newToken, refreshToken);
            tokenService.createToken(newTokenEntity);

            response.setHeader("X-AUTH-TOKEN", newToken); // tokeをX-AUTH-TOKENというKeyにセットする
            response.setStatus(200);
        }

        // ログイン状態を設定する
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username,null,new ArrayList<>()));
        filterChain.doFilter(request,response);
    }
}