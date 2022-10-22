package com.group.sampleproject.controller;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.sampleproject.entity.AttendanceEntity;
import com.group.sampleproject.entity.UserEntity;
import com.group.sampleproject.repository.AttendanceRepository;
import com.group.sampleproject.service.TokenService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@EnableConfigurationProperties
public class SampleControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TokenService tokenService;

    private UserEntity testUser = new UserEntity("newUser4", "test@test.com", "testPassword", "USER");

    @MockBean
    AttendanceRepository attendanceRepository;

        @Test
        void sampleGetTest () throws Exception{
                
            mockMvc.perform(get("/testA"))
            .andExpect(status().isOk())
            // 指定したデータが指定の形式で含まれるか
            .andExpect(jsonPath("$.username").value("zenn"));
        }    

        @Test
        void loginStatusTestInUnautherizedCase () throws Exception{
                
            mockMvc.perform(get("/api/test"))
            .andExpect(status().isForbidden());
        }    

        @Test
        void loginStatusTestInAutherizedCase () throws Exception{

            String token = tokenService.generateToken(testUser.getUsername(), tokenService.getDatePlusMin(10));

            mockMvc.perform(
                get("/api/test")
                .header("X-AUTH-TOKEN", "Bearer " + token)
            )
            .andExpect(status().isOk())
            // 指定したデータが指定の形式で含まれるか
            .andExpect(content().string(org.hamcrest.Matchers.containsString("認証が成功しています")));
        }    

        @Test
        void loginStatusTestInAutherizedCaseWithOldToken () throws Exception{

            String token = tokenService.generateToken(testUser.getUsername(), tokenService.getDatePlusMin(-10));

            mockMvc.perform(
                get("/api/test")
                .header("X-AUTH-TOKEN", "Bearer " + token)
            )
            .andExpect(status().isForbidden());
        }   

        @Test
        void getAttendanceList () throws Exception{

            String token = tokenService.generateToken(testUser.getUsername(), tokenService.getDatePlusMin(10));
            ObjectMapper mapper = new ObjectMapper();
            
            //モック用のデータ
            List<AttendanceEntity> attendanceList = new ArrayList<>();
            AttendanceEntity attendance = new AttendanceEntity(1, new Date(), new Date(), token, null, testUser, "title", "red");
            attendanceList.add(attendance);
            when(attendanceRepository.findByUserId(anyInt())).thenReturn(attendanceList);
            
            //検証用のデータ
            String expectedJsonResult = mapper.writeValueAsString(List.of(attendance.getCalendarEvent()));

            mockMvc.perform(
                get("/api/attendance")
                .header("X-AUTH-TOKEN", "Bearer " + token)
            )
            .andExpect(status().isOk())
            //jsonごと検証する場合
            .andExpect(content().json(expectedJsonResult))
            //jsonの中に期待値が含まれているか確認したい場合
            .andExpect(content().string(org.hamcrest.Matchers.containsString(expectedJsonResult)))
            //jsonの各項目ごとに検証する場合
            .andExpect(jsonPath("$.[0].title").value("title"));

        }   
}
