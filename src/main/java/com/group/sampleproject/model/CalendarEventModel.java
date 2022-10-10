package com.group.sampleproject.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.group.sampleproject.entity.AttendanceEntity;
import com.group.sampleproject.entity.TagEntity;
import com.group.sampleproject.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalendarEventModel {

    private int id;
    private String title;
    private String start;
    private String end;    
    private String comment;

    /** 
     * CalendarEventModelに変換して取得
     * @return CalendarEvent
     */
    public AttendanceEntity toAttendanceEntity(UserEntity user){

        Date startDate = null;
        Date endDate = null;
        try {
                            
            DateFormat formatter = new SimpleDateFormat(AttendanceEntity.DATE_PATTERN);
            startDate = formatter.parse(start);
            endDate = formatter.parse(end);
                
        } catch (Exception e) {
        }

        return new AttendanceEntity(id,startDate,endDate,comment,new ArrayList<TagEntity>(),user,title);

    }
}
