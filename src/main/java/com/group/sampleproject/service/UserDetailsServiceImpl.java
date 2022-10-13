package com.group.sampleproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.group.sampleproject.entity.CustomUserDetail;
import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            UserEntity entity = userRepository.findByName(username);
            // 認可があればここで設定できる
            //CustomUserDetailを使用する
            return new CustomUserDetail (entity);
            // return new User(entity.getUsername(), PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(entity.getPassword()), new ArrayList<>());
        }catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりません");
        }
    }
}