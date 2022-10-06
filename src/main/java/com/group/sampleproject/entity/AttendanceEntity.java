package com.group.sampleproject.entity;

import java.util.Date;
import java.util.List;

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
    // private List<TagAttendanceEntity> tagAttendanceEntityList;
}
