package com.project.schoolmanagment.payload.mappers.user;

import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
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
}
