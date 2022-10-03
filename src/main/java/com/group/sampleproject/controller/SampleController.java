package com.group.sampleproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.model.SampleObject;
import com.group.sampleproject.model.User;
import com.group.sampleproject.payload.request.SampleForm;
import com.group.sampleproject.repository.UserRepository;

@RestController
public class SampleController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sample")
    public SampleObject post(@RequestBody SampleForm sampleForm){
        int id = sampleForm.getId();
        SampleObject resultObject = new SampleObject();
        resultObject.setName("zenn");
        resultObject.setLank(id); //idが取得できたか確認しているだけ
        return resultObject;
    }

    // 追加
    @GetMapping("/test")
    public User get(){
        UserEntity userEntity = this.userRepository.findByName("zenn");
        return userEntity.toUser();
    }
}