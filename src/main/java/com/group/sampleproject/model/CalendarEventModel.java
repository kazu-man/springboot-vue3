package com.group.sampleproject.model;

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
    private String url;

}
