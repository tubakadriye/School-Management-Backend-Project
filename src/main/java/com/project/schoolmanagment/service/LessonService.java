package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.Lesson;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
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
		isLessonExistByLessonName(lessonRequest.getLessonName());

		Lesson savedLesson = lessonRepository.save(lessonDto.mapLessonRequestToLesson(lessonRequest));

		return ResponseMessage.<LessonResponse>builder()
				.object(lessonDto.mapLessonToLessonResponse(savedLesson))
				.message("Lesson Created Successfully")
				.httpStatus(HttpStatus.CREATED)
				.build();
	}


	public ResponseMessage deleteLessonById(Long id){
		isLessonExistById(id);
		lessonRepository.deleteById(id);

		return ResponseMessage.builder()
				.message("Lesson is deleted successfully")
				.httpStatus(HttpStatus.OK)
				.build();
	}


	private boolean isLessonExistByLessonName(String lessonName){
		boolean lessonExist = lessonRepository.existsLessonByLessonNameEqualsIgnoreCase(lessonName);
		if(lessonExist){
			throw new ResourceNotFoundException(String.format(Messages.ALREADY_REGISTER_LESSON_MESSAGE, lessonName));
		} else {
			return false;
		}
	}

	private void isLessonExistById(Long id){
		lessonRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_MESSAGE, id)));
	}



}
