package com.group.sampleproject.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.group.sampleproject.entity.Token;
import com.group.sampleproject.service.TokenService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        
        // headerからTokenを取得する
        String authHeader = request.getHeader("X-AUTH-TOKEN");
        
        //　チェック処理
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        
        response.setStatus(403);
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
            if (tokenEntitity == null) return;

            String refreshToken = tokenEntitity.getRefreshToken();
            //refreshTokenの検証
            decodedJWT = JWT.require(Algorithm.HMAC256("secret")).build().verify(refreshToken);

            Map<String,String> tokens = tokenService.createNewTokens(username);
            String newToken = tokens.get("token");     
            refreshToken = tokens.get("refreshToken");     
            
            //古いtokenを削除
            tokenService.deleteToken(tokenEntitity);

            response.setHeader("X-AUTH-TOKEN", newToken); // tokeをX-AUTH-TOKENというKeyにセットする
            
        }

        response.setStatus(200);

        // ログイン状態を設定する
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,new ArrayList<>());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }
}