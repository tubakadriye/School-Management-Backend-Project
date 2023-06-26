package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.Lesson;
import com.project.schoolmanagment.entity.concretes.Student;
import com.project.schoolmanagment.entity.concretes.Teacher;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.request.StudentInfoRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.StudentInfoResponse;
import com.project.schoolmanagment.repository.StudentInfoRepository;
import com.project.schoolmanagment.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

	private final StudentService studentService;
	private final TeacherService teacherService;
	private final EducationTermService educationTermService;
	private final LessonService lessonService;
	private final StudentInfoRepository studentInfoRepository;
	@Value("${midterm.exam.impact.percentage}")
	private Double midtermExamPercentage;
	@Value("${final.exam.impact.percentage}")
	private Double finalExamPercentage;

	public ResponseMessage<StudentInfoResponse>saveStudentInfo(String teacherUsername, StudentInfoRequest studentInfoRequest){
		// we need entity for creation of StudentInfo
		Student student = studentService.isStudentsExist(studentInfoRequest.getStudentId());
		Teacher teacher = teacherService.getTeacherByUsername(teacherUsername);
		Lesson lesson = lessonService.isLessonExistById(studentInfoRequest.getLessonId());

		//does student really have only one lesson accordind to this lesson name
		checkSameLesson(studentInfoRequest.getStudentId(), lesson.getLessonName());
		//we need grade calculation
	}


	private void checkSameLesson(Long studentId,String lessonName){
		boolean isLessonDupliucationExist =  studentInfoRepository.getAllByStudentId_Id(studentId)
				.stream()
				.anyMatch((e)->e.getLesson().getLessonName().equalsIgnoreCase(lessonName));

		if(isLessonDupliucationExist){
			throw new ConflictException(String.format(Messages.ALREADY_REGISTER_LESSON_MESSAGE,lessonName));
		}
	}



	private Double calculateExamAverage(Double midtermExam, Double finalExam){
		return ((midtermExam* midtermExamPercentage) + (finalExam*finalExamPercentage));
	}


}
