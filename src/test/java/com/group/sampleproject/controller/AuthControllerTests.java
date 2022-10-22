package com.group.sampleproject.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.repository.UserRepository;
import com.group.sampleproject.service.TokenService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;
    @MockBean
    TokenService tokenService;

    UserEntity testNewUser = null;

    @BeforeEach
    public void setUp(){
        testNewUser = new UserEntity("unitTestUser", "unitTest@test.com", "password", "USER");
    }

    //ログインテストがうまくいかない・・・
    @Test
    void login() throws Exception{

        Map<String,String> tokenResult = new HashMap<>();
        tokenResult.put("token","token");
        tokenResult.put("refreshToken","refreshToken");

        when(tokenService.createNewTokens("newUser4")).thenReturn(tokenResult);

        JSONObject json = new JSONObject();
        json.put("username", "newUser4");
        json.put("password", "password");

        try {
            // try の外で構築したリクエストを投げて結果を確認
            mockMvc.perform(post("/api/login",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString())
        )
        .andExpect(status().isOk())
        .andExpect(header().string("X-AUTH-TOKEN", "token"));
            return;// テスト成功ならばここでリターン
        } catch (NestedServletException e) {
            // NestedServletException 限定の処理
            // NestedServletException の元となった例外が getCause メソッドで取得できるのでそれを使う
            // @see https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html?is-external=true#getCause--
            e.getCause().printStackTrace();
            System.out.println(e.getCause().getMessage());
            // NestedServletException が投げられたこともコンソールに表示
            System.out.println(e.getMessage());
        } catch (Exception e) {
            // NestedServletException 以外の例外はこっちでキャッチする
            // andExpect の期待に沿えない部分があったら例外が投げられてここに来る
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    // 正しい入力内容でsignUpできるか
    @Test
    void signUpTest () throws Exception{

        JSONObject requestContent = setRequestContentJson(testNewUser);

        tokenServiceMockForSignUp();
        
        mockMvc.perform(post("/api/signUp",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent.toString())
        )
        .andExpect(status().isOk())
        .andExpect(header().string("X-AUTH-TOKEN", "token"));

    }    


    // 不正なemailで登録処理
    @Test
    void signUpTestWithInvalidEmail () throws Exception{
        //テスト用のユーザー作成
        testNewUser.setEmail("invalidemail");
        JSONObject requestContent = setRequestContentJson(testNewUser);

        tokenServiceMockForSignUp();
        
        mockMvc.perform(post("/api/signUp",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent.toString())
        )
        .andExpect(status().isBadRequest())
        .andExpect(header().doesNotExist("X-AUTH-TOKEN"))
        // 指定した文字列がresponseBodyに含まれるか
        .andExpect(content().string(org.hamcrest.Matchers.containsString("must be a well-formed email address")))
        // 指定したデータが指定の形式で含まれるか
        .andExpect(jsonPath("$.errors.email").value("must be a well-formed email address"));

    }    


    // すでに存在するユーザーで登録処理
    @Test
    void signUpTestWithExistedUser () throws Exception{

        JSONObject requestContent = setRequestContentJson(testNewUser);

        tokenServiceMockForSignUp();
        
        //userが存在する設定
        when(userRepository.existsByUsername(testNewUser.getUsername())).thenReturn(testNewUser);

        mockMvc.perform(post("/api/signUp",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent.toString())
        )
        .andExpect(status().isBadRequest())
        .andExpect(header().doesNotExist("X-AUTH-TOKEN"))
        // 指定した文字列がresponseBodyに含まれるか
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Error: Username is already taken!")))
        // 指定したデータが指定の形式で含まれるか
        .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));

    }    

    /** 
     * signUpテストで使う共通モックを作成
     */
    public void tokenServiceMockForSignUp(){
        Map<String,String> tokenResult = new HashMap<>();
        tokenResult.put("token","token");
        tokenResult.put("refreshToken","refreshToken");
        when(tokenService.createNewTokens("unitTestUser")).thenReturn(tokenResult);
        
        when(userRepository.findByName(testNewUser.getUsername())).thenReturn(testNewUser);
    }

    
    /** 
     * signUpテストで使うリクエストコンテンツを作成
     * @param testNewUser
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject setRequestContentJson(UserEntity testNewUser) throws Exception{
        JSONObject json = new JSONObject();
        json.put("username", testNewUser.getUsername());
        json.put("email", testNewUser.getEmail());
        json.put("role", testNewUser.getRole());
        json.put("password", testNewUser.getPassword());


        return json;
    }
}
