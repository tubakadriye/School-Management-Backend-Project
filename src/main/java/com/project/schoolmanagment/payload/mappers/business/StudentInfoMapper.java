package com.project.schoolmanagment.payload.mappers.business;

import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.entity.concretes.business.Lesson;
import com.project.schoolmanagment.entity.concretes.business.StudentInfo;
import com.project.schoolmanagment.entity.enums.Note;
import com.project.schoolmanagment.payload.mappers.user.StudentMapper;
import com.project.schoolmanagment.payload.request.business.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.business.UpdateStudentInfoRequest;
import com.project.schoolmanagment.payload.response.business.StudentInfoResponse;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class StudentInfoMapper {
    @Autowired
    private final StudentMapper studentMapper;

    public StudentInfo mapStudentInfoRequestToStudentInfo(StudentInfoRequest studentInfoRequest,
                                                          Note note,
                                                          Double average){
        return StudentInfo.builder()
                .infoNote(studentInfoRequest.getInfoNote())
                .absentee(studentInfoRequest.getAbsentee())
                .midtermExam(studentInfoRequest.getMidtermExam())
                .finalExam(studentInfoRequest.getFinalExam())
                .letterGrade(note)
                .examAverage(average)
                .build();
    }

    public StudentInfoResponse mapStudentInfoToStudentInfoResponse(StudentInfo studentInfo){
        return StudentInfoResponse.builder()
                .lessonName(studentInfo.getLesson().getLessonName())
                .creditScore(studentInfo.getLesson().getCreditScore())
                .isCompulsory(studentInfo.getLesson().getIsCompulsory())
                .educationTerm(studentInfo.getEducationTerm().getTerm())
                .id(studentInfo.getId())
                .absentee(studentInfo.getAbsentee())
                .midtermExam(studentInfo.getMidtermExam())
                .finalExam(studentInfo.getFinalExam())
                .infoNote(studentInfo.getInfoNote())
                .note(studentInfo.getLetterGrade())
                .average(studentInfo.getExamAverage())
                .studentResponse(studentMapper.mapStudentToStudentResponse(studentInfo.getStudent()))
                .build();
    }

    public StudentInfo mapStudentInfoUpdateToStudentInfo(UpdateStudentInfoRequest updateStudentInfoRequest,
                                                         Long studentInfoRequestId,
                                                         Lesson lesson,
                                                         EducationTerm educationTerm,
                                                         Note note,
                                                         Double average){
        return StudentInfo.builder()
                .id(studentInfoRequestId)
                .infoNote(updateStudentInfoRequest.getInfoNote())
                .midtermExam(updateStudentInfoRequest.getMidtermExam())
                .finalExam(updateStudentInfoRequest.getFinalExam())
                .absentee(updateStudentInfoRequest.getAbsentee())
                .lesson(lesson)
                .educationTerm(educationTerm)
                .examAverage(average)
                .letterGrade(note)
                .build();
    }
}
