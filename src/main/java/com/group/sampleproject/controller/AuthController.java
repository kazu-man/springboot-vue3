package com.group.sampleproject.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.model.LoginUserModel;
import com.group.sampleproject.payload.request.LoginForm;
import com.group.sampleproject.payload.request.SignUpRequest;
import com.group.sampleproject.payload.response.MessageResponse;
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
    @Autowired
	BCryptPasswordEncoder encoder;
    
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
        
        Map<String,String> tokens = tokenService.createNewTokens(authentication.getName());
        String token = tokens.get("token");     
    
        //ヘッダーにトークンをセット
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-AUTH-TOKEN", token);

        UserEntity user = userRepository.findByName(form.getUsername());

        //ログインユーザ情報をbodyに詰めて返却
        return ResponseEntity.ok().headers(headers).body(user.toLoginUserModel());

    }


	@PostMapping("/api/signUp")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername()) != null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail()) != null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		// Create new user's account
		UserEntity user = new UserEntity(
                                    signUpRequest.getUsername(), 
                                    signUpRequest.getEmail(),
                                    encoder.encode(signUpRequest.getPassword()),
                                    signUpRequest.getRole()
							 );
		//ユーザーを保存
        userRepository.save(user);
        user = userRepository.findByName(user.getUsername());

        //すぐログインできるようにトークンを生成
        Map<String,String> tokens = tokenService.createNewTokens(signUpRequest.getUsername());
        String token = tokens.get("token"); 

        //ヘッダーにトークンをセット
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-AUTH-TOKEN", token);

        //ログインユーザ情報をbodyに詰めて返却
        return ResponseEntity.ok().headers(headers).body(user.toLoginUserModel());
	}
}
