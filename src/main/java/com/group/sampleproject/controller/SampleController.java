package com.group.sampleproject.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group.sampleproject.model.SampleObject;
import com.group.sampleproject.payload.request.SampleForm;

@RestController
public class SampleController {

    @PostMapping("/sample")
    public SampleObject post(@RequestBody SampleForm sampleForm){
        int id = sampleForm.getId();
        SampleObject resultObject = new SampleObject();
        resultObject.setName("zenn");
        resultObject.setLank(id); //idが取得できたか確認しているだけ
        return resultObject;
    }
}