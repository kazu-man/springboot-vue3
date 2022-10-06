package com.group.sampleproject.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagEntity {
    private int id;
    private AttendanceEntity attendanceEntity;
    private String tagName;    
}
