package com.project.schoolmanagment.service;

import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.request.LessonRequest;
import com.project.schoolmanagment.payload.response.LessonResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.LessonRepository;
import com.project.schoolmanagment.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {



	private final LessonRepository lessonRepository;


	public ResponseMessage<LessonResponse>saveLesson(LessonRequest lessonRequest){
		if(isLessonExist(lessonRequest.getLessonName())){
			throw new ConflictException(Messages.)
		}
	}


	private boolean isLessonExist(String lessonName){
		return lessonRepository.existsLessonByLessonNameEqualsIgnoreCase(lessonName);
	}



}
