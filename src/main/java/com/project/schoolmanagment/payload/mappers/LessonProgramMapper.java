package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.businnes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.businnes.Lesson;
import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class LessonProgramMapper {


	public LessonProgram mapLessonProgramRequestToLessonProgram(LessonProgramRequest lessonProgramRequest, Set<Lesson> lessonSet, EducationTerm educationTerm){
		return LessonProgram.builder()
				.startTime(lessonProgramRequest.getStartTime())
				.stopTime(lessonProgramRequest.getStopTime())
				.day(lessonProgramRequest.getDay())
				.lessons(lessonSet)
				.educationTerm(educationTerm)
				.build();
	}


	public LessonProgramResponse mapLessonProgramtoLessonProgramResponse(LessonProgram lessonProgram){
		return LessonProgramResponse.builder()
				.day(lessonProgram.getDay())
				.startTime(lessonProgram.getStartTime())
				.stopTime(lessonProgram.getStopTime())
				.lessonProgramId(lessonProgram.getId())
				.lessonName(lessonProgram.getLessons())
				.build();
	}
}
