package com.group.sampleproject.service;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.model.LoginUserModel;
import com.group.sampleproject.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void getLoginUserByUsername (){

        UserEntity user = new UserEntity("taro", "taroemail", "taroPassword", "USER");
        user.setId(1);
        when(userRepository.findByName("taro")).thenReturn(user);

        LoginUserModel retrunedUser = userService.getLoginUserByUsername("taro");

        assertThat(retrunedUser.getId()).isEqualTo(1);
        assertThat(retrunedUser.getUsername()).isEqualTo("taro");
        assertThat(retrunedUser.getEmail()).isEqualTo("taroemail");
        assertThat(retrunedUser.getRole()).isEqualTo("USER");
    }
    
}
