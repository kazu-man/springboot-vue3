package com.group.sampleproject.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group.sampleproject.entity.Token;
import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.model.LoginUserModel;
import com.group.sampleproject.payload.request.LoginForm;
import com.group.sampleproject.repository.UserRepository;
import com.group.sampleproject.service.TokenService;

@RestController
public class AuthController {

    @Autowired
    UserRepository userRepository;
    @Autowired
	AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;
    
    @PostMapping("/api/login")
    public ResponseEntity<LoginUserModel> login(@RequestBody LoginForm form){

        Authentication authentication = null;
        try{
            //認証処理
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword(), new ArrayList<>())
                );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new LoginUserModel());
        }
        
        // トークンの期限を取得
        Date tokenExTime = tokenService.getDatePlusMin(1);
        Date refreshTokenExTime = tokenService.getDatePlusMin(5);

        // // トークンの作成
        String token = tokenService.generateToken(authentication.getName(), tokenExTime);
        // // リフレッシュトークンの作成
        String refreshToken = tokenService.generateToken(authentication.getName(), refreshTokenExTime);
                
        //refreshToken　の保存
        Token newTokenEntity = new Token(token, refreshToken);
        tokenService.createToken(newTokenEntity);        
    
        //ヘッダーにトークンをセット
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-AUTH-TOKEN", token);

        UserEntity user = userRepository.findByName(form.getUsername());

        //ログインユーザ情報をbodyに詰めて返却
        return ResponseEntity.ok().headers(headers).body(user.toLoginUserModel());

    }
}
