package com.group.sampleproject.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group.sampleproject.entity.AttendanceEntity;
import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.model.CalendarEventModel;
import com.group.sampleproject.model.SampleObject;
import com.group.sampleproject.payload.request.SampleForm;
import com.group.sampleproject.repository.AttendanceRepository;
import com.group.sampleproject.repository.UserRepository;

@RestController
public class SampleController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/sample")
    public SampleObject post(@RequestBody SampleForm sampleForm){
        int id = sampleForm.getId();
        SampleObject resultObject = new SampleObject();
        resultObject.setName("zenn");
        resultObject.setLank(id); //idが取得できたか確認しているだけ
        return resultObject;
    }

    // 追加
    @GetMapping("/testA")
    public UserEntity get(){
        UserEntity userEntity = this.userRepository.findByName("zenn");
        return userEntity;
    }

    // 追加
    @GetMapping("/testB")
    public AttendanceEntity getAttendance(){
        AttendanceEntity attendanceEntity = this.attendanceRepository.findById(1);
        return attendanceEntity;
    }
    
    @GetMapping("/api/test")
    public String test(){
        return "認証が成功しています";
    }	

    
    
    /** 
     * Userに紐づくattendanceからcalendarに表示するデータ取得
     * @param user
     * @return List<CalendarEventModel>
     */
    @PostMapping("/api/attendanceByUserId")
    public List<CalendarEventModel> attendanceByUserId(@RequestBody UserEntity user){

        List<AttendanceEntity> attendanceList = attendanceRepository.findByUserId(user.getId());
        
        //vueで表示用のカレンダーデータモデルに変換する
        List<CalendarEventModel> calEvent = attendanceList.stream().map(AttendanceEntity::getCalendarEvent).collect(Collectors.toList());
        return calEvent;

    }
}