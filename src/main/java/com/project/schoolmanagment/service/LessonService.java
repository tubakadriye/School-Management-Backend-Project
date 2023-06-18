package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.Lesson;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.mappers.LessonDto;
import com.project.schoolmanagment.payload.request.LessonRequest;
import com.project.schoolmanagment.payload.response.LessonResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.LessonRepository;
import com.project.schoolmanagment.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {


	private final LessonDto lessonDto;

	private final LessonRepository lessonRepository;


	public ResponseMessage<LessonResponse>saveLesson(LessonRequest lessonRequest){
		isLessonExist(lessonRequest.getLessonName());

		Lesson savedLesson = lessonRepository.save(lessonDto.mapLessonRequestToLesson(lessonRequest));

		return ResponseMessage.<LessonResponse>builder()
				.object(lessonDto.mapLessonToLessonResponse(savedLesson))
				.message("Lesson Created Successfully")
				.httpStatus(HttpStatus.CREATED)
				.build();
	}


	private boolean isLessonExist(String lessonName){
		boolean lessonExist = lessonRepository.existsLessonByLessonNameEqualsIgnoreCase(lessonName);
		if(lessonExist){
			throw new ConflictException(String.format(Messages.ALREADY_REGISTER_LESSON_MESSAGE, lessonName));
		} else {
			return false;
		}
	}



}
