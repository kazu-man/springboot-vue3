package com.group.sampleproject.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.sampleproject.entity.Token;
import com.group.sampleproject.payload.request.LoginForm;
import com.group.sampleproject.service.TokenService;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    
    public JsonAuthenticationFilter(AuthenticationManager authenticationManager, TokenService tokenService){
    
        // AuthenticationManagerの設定
        this.authenticationManager = authenticationManager;
    
        // ログインパスを設定
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login","POST"));
        // ログイン用パラメータの設定
        setUsernameParameter("username");
        setPasswordParameter("password");
    
        // ログイン成功時はtokenを発行してレスポンスにセットする
        this.setAuthenticationSuccessHandler((req,res,ex) -> {

            Calendar cal = Calendar.getInstance();

            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, 1);
            Date tokenExTime = cal.getTime();
            cal.add(Calendar.MINUTE, 5);
            Date refreshTokenExTime = cal.getTime();
            
            // // トークンの作成
            String token = tokenService.generateToken(ex.getName(), tokenExTime);
            // // リフレッシュトークンの作成
            String refreshToken = tokenService.generateToken(ex.getName(), refreshTokenExTime);
                    
            res.setHeader("X-AUTH-TOKEN", token); // tokeをX-AUTH-TOKENというKeyにセットする
            res.setStatus(200);

            //refreshToken　の保存
            Token newTokenEntity = new Token(token, refreshToken);
            tokenService.createToken(newTokenEntity);
        });
    
        // ログイン失敗時
        this.setAuthenticationFailureHandler((req,res,ex) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // リクエストのjsonの値をLoginFormにマッピングします。
            LoginForm form = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);
            // これでデフォルトのProviderを利用しつつ、ユーザーレコードの取得に関してはUserDetailsServiceの実装クラスのloadUserByUsernameを利用する
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }