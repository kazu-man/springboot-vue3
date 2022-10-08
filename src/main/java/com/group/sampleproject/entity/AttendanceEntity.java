package com.group.sampleproject.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.group.sampleproject.model.CalendarEventModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEntity {
    private int id;
    private UserEntity userEntity;
    private Date startAt;
    private Date finishAt;
    private String comment;
    private List<TagEntity> tagEntityList;
    static public final String DATE_PATTERN ="yyyy-MM-dd HH:mm:ss";

    
    /** 
     * CalendarEventModelに変換して取得
     * @return CalendarEvent
     */
    public CalendarEventModel getCalendarEvent(){

        String stringStartAt = new SimpleDateFormat(DATE_PATTERN).format(startAt);
        String stringFinishtAt = new SimpleDateFormat(DATE_PATTERN).format(finishAt);
        String title = "sample title";
        String url = "sample.com";

        return new CalendarEventModel(id,title,stringStartAt,stringFinishtAt,url);
    }
}