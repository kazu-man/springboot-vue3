package com.group.sampleproject.entity;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagAttendanceEntity {
    private int id;
    private AttendanceEntity attendanceEntity;
    private List<TagEntity> tagEntityList;
}
