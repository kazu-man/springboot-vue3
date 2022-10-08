package com.group.sampleproject.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.group.sampleproject.entity.AttendanceEntity;

@Mapper
public interface AttendanceRepository {
    AttendanceEntity findById(int id);    
    List<AttendanceEntity> findByUserId(int userId);
}
