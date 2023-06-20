package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.Lesson;
import com.project.schoolmanagment.entity.concretes.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.LessonProgramDto;
import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.LessonProgramRepository;
import com.project.schoolmanagment.utils.Messages;
import com.project.schoolmanagment.utils.TimeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

	private final LessonProgramRepository lessonProgramRepository;

	private final LessonService lessonService;

	private final EducationTermService educationTermService;

	private final LessonProgramDto lessonProgramDto;


	public ResponseMessage<LessonProgramResponse>saveLessonProgram(LessonProgramRequest lessonProgramRequest){

		Set<Lesson> lessons = lessonService.getLessonByLessonIdSet(lessonProgramRequest.getLessonIdList());

		EducationTerm educationTerm = educationTermService.getEducationTermById(lessonProgramRequest.getEducationTermId());

		if(lessons.size()==0){
			throw new ResourceNotFoundException(Messages.NOT_FOUND_LESSON_IN_LIST);
		} else if (TimeControl.checkTime(lessonProgramRequest.getStartTime(),lessonProgramRequest.getStopTime())) {
			throw new BadRequestException(Messages.TIME_NOT_VALID_MESSAGE);
		}

		LessonProgram lessonProgram = lessonProgramDto.mapLessonProgramRequestToLessonProgram(lessonProgramRequest,lessons,educationTerm);

		LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);

		return ResponseMessage.<LessonProgramResponse>builder()
				.message("Lesson Program is Created")
				.httpStatus(HttpStatus.CREATED)
				.object(lessonProgramDto.mapLessonProgramtoLessonProgramResponse(savedLessonProgram))
				.build();
	}


	public List<LessonProgramResponse> getAllLessonProgramList(){
		return lessonProgramRepository
				.findAll()
				.stream()
				.map(lessonProgramDto::mapLessonProgramtoLessonProgramResponse)
				.collect(Collectors.toList());
	}

	public LessonProgramResponse getLessonProgramById(Long id){
		isLessonProgramExistById(id);
		return lessonProgramDto.mapLessonProgramtoLessonProgramResponse(lessonProgramRepository.findById(id).get());
	}

	public List<LessonProgramResponse>getAllLessonProgramUnassigned(){
			return lessonProgramRepository.findByTeachers_IdNull()
					.stream()
					.map(lessonProgramDto::mapLessonProgramtoLessonProgramResponse)
					.collect(Collectors.toList());
	}

	public List<LessonProgramResponse>getAllAssigned(){
		return lessonProgramRepository.findByTeachers_IdNotNull()
				.stream()
				.map(lessonProgramDto::mapLessonProgramtoLessonProgramResponse)
				.collect(Collectors.toList());
	}

	public ResponseMessage deleteLessonProgramById(Long id) {
		isLessonProgramExistById(id);
		lessonProgramRepository.deleteById(id);
		return ResponseMessage.builder()
				.message("Lesson program is deleted successfully")
				.httpStatus(HttpStatus.OK)
				.build();
	}

	private void isLessonProgramExistById(Long id){
		lessonProgramRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_PROGRAM_MESSAGE,id)));
	}

}
