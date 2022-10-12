package com.group.sampleproject.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.group.sampleproject.model.CalendarEventModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity {

    static public final String DATE_PATTERN ="yyyy-MM-dd HH:mm:ss";

    private int id;
    private UserEntity userEntity;
    private Date startAt;
    private Date finishAt;
    private String comment;
    private List<TagEntity> tagEntityList;
    private String title;
    private String backgroundColor;
        
    public AttendanceEntity(int id, Date startAt, Date finishAt, String comment, ArrayList<TagEntity> tagEntityList,
            UserEntity userEntity,String title,String backgroundColor) {

        this.id = id;
        this.startAt = startAt;
        this.finishAt = finishAt;
        this.comment = comment;
        this.tagEntityList = tagEntityList;
        this.userEntity = userEntity;
        this.title = title;
        this.backgroundColor = backgroundColor;
    }
    /** 
     * CalendarEventModelに変換して取得
     * @return CalendarEvent
     */
    public CalendarEventModel getCalendarEvent(){

        String stringStartAt = new SimpleDateFormat(DATE_PATTERN).format(startAt);
        String stringFinishtAt = new SimpleDateFormat(DATE_PATTERN).format(finishAt);

        return new CalendarEventModel(id,title,stringStartAt,stringFinishtAt,comment,backgroundColor);
    }
}