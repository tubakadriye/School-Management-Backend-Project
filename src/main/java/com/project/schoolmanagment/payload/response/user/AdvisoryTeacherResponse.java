package com.project.schoolmanagment.payload.response.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvisoryTeacherResponse {

    private Long advisoryTeacherId;
    private String teacherName;
    private String teacherSnn;
    private String teacherSurname;
}
