package com.group.sampleproject.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group.sampleproject.entity.AttendanceEntity;
import com.group.sampleproject.entity.CustomUserDetail;
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
    public List<CalendarEventModel> attendanceByUserId(@AuthenticationPrincipal CustomUserDetail loginUser,@RequestBody UserEntity user){

        List<CalendarEventModel> calendarList = new ArrayList<>();
        
        List<AttendanceEntity> attendanceList = attendanceRepository.findByUserId(user.getId());
        //データが見つからない場合、1件目がnullのリストを返されるので対策
        attendanceList.removeAll(Collections.singleton(null));  

        if(!attendanceList.isEmpty()){
            //vueで表示用のカレンダーデータモデルに変換する
            calendarList=  attendanceList.stream().map(AttendanceEntity::getCalendarEvent).collect(Collectors.toList());
        }

        return calendarList;
    }

    @PostMapping("/api/attendance")
    public ResponseEntity<CalendarEventModel>  createAttendance(@AuthenticationPrincipal CustomUserDetail loginUser,@RequestBody CalendarEventModel event){
        AttendanceEntity attendance = event.toAttendanceEntity(loginUser.getUserEntity());
        attendanceRepository.createAttendance(attendance);

        return ResponseEntity.ok().body(event);
    }

    @PutMapping("/api/attendance")
    public ResponseEntity<CalendarEventModel>  updateAttendance(@AuthenticationPrincipal CustomUserDetail loginUser,@RequestBody CalendarEventModel event){
        AttendanceEntity attendance = event.toAttendanceEntity(loginUser.getUserEntity());
        attendanceRepository.updateAttendance(attendance);

        return ResponseEntity.ok().body(event);
    }
}