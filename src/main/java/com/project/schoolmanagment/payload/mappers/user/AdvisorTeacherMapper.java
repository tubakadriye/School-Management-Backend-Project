package com.project.schoolmanagment.payload.mappers.user;

import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.payload.response.user.AdvisoryTeacherResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AdvisorTeacherMapper {

    public AdvisoryTeacher mapTeacherToAdvisorTeacher(Teacher teacher ) {
        return AdvisoryTeacher.builder()
                .teacher(teacher)
                .build();
    }

    public AdvisoryTeacherResponse mapAdvisorTeacherToAdvisoryTeacherResponse(AdvisoryTeacher advisoryTeacher){
        return AdvisoryTeacherResponse.builder()
                .advisoryTeacherId(advisoryTeacher.getId())
                .teacherName(advisoryTeacher.getTeacher().getName())
                .teacherSurname(advisoryTeacher.getTeacher().getSurname())
                .teacherSnn(advisoryTeacher.getTeacher().getSsn())
                .build();
    }
}
