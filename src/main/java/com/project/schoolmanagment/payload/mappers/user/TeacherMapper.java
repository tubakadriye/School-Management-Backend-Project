package com.project.schoolmanagment.payload.mappers.user;

import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;

public class TeacherMapper {

    public Teacher mapTeacherRequestToTeacher(TeacherRequest teacherRequest) {
        return Teacher.builder()
                .name(teacherRequest.getName())
                .surname(teacherRequest.getSurname())
                .ssn(teacherRequest.getSsn())
                .username(teacherRequest.getUsername())
                .birthDay(teacherRequest.getBirthDay())
                .birthPlace(teacherRequest.getBirthPlace())
                .password(teacherRequest.getPassword())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .email(teacherRequest.getEmail())
                .isAdvisory(teacherRequest.getIsAdvisorTeacher())
                .gender(teacherRequest.getGender())
                .build();

    }


    public Teacher mapTeacherRequestToUpdatedTeacher(TeacherRequest teacherRequest, Long id){
        Teacher teacher = mapTeacherRequestToTeacher(teacherRequest);
        teacher.setId(id);
        return teacher;

    }


    public TeacherResponse mapTeacherToTeacherResponse(Teacher teacher) {

        return TeacherResponse.builder()
                .userId(teacher.getId())
                .username(teacher.getUsername())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .birthDay(teacher.getBirthDay())
                .birthPlace(teacher.getBirthPlace())
                .ssn(teacher.getSsn())
                .phoneNumber(teacher.getPhoneNumber())
                .gender(teacher.getGender())
                .email(teacher.getEmail())
                .build();
    }
}
