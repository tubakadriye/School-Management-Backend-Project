package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.Lesson;
import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.LessonProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

	private final LessonProgramRepository lessonProgramRepository;

	private final LessonService lessonService;

	private final EducationTermService educationTermService;


	public ResponseMessage<LessonProgramResponse>saveLessonProgram(LessonProgramRequest lessonProgramRequest){

		Set<Lesson> lessons = lessonService.getLessonByLessonIdSet(lessonProgramRequest.getLessonIdList());

		EducationTerm educationTerm = educationTermService.getEducationTermById(lessonProgramRequest.getEducationTermId());


	}
}
