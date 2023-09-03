package com.project.schoolmanagment.payload.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.schoolmanagment.entity.concretes.business.LessonProgram;
import com.project.schoolmanagment.payload.response.abstracts.BaseUserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL) //IF IT'S NOT NULL PLEASE MAP
public class StudentResponse extends BaseUserResponse {

    private Set<LessonProgram>lessonProgramSet;
    private int studentNumber;
    private String motherName;
    private String fatherName;
    private String email;
    private boolean isActive;
}
