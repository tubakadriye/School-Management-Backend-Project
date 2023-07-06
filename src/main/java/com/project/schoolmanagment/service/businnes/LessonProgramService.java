package com.project.schoolmanagment.service.businnes;

import com.project.schoolmanagment.entity.concretes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.Lesson;
import com.project.schoolmanagment.entity.concretes.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.LessonProgramMapper;
import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.LessonProgramRepository;
import com.project.schoolmanagment.payload.responsemessages.ErrorMessages;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import com.project.schoolmanagment.service.validator.TimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	private final LessonProgramMapper lessonProgramMapper;

	private final UniquePropertyValidator uniquePropertyValidator;

	private final PageableHelper pageableHelper;


	public ResponseMessage<LessonProgramResponse>saveLessonProgram(LessonProgramRequest lessonProgramRequest){

		Set<Lesson> lessons = lessonService.getLessonByLessonIdSet(lessonProgramRequest.getLessonIdList());

		EducationTerm educationTerm = educationTermService.getEducationTermById(lessonProgramRequest.getEducationTermId());

		if(lessons.size()==0){
			throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LESSON_IN_LIST);
		}
		// old usage
//		else if (TimeControl.checkTime(lessonProgramRequest.getStartTime(),lessonProgramRequest.getStopTime())) {
//			throw new BadRequestException(Messages.TIME_NOT_VALID_MESSAGE);
//		}
		TimeValidator.checkTimeWithException(lessonProgramRequest.getStartTime(),lessonProgramRequest.getStopTime());

		LessonProgram lessonProgram = lessonProgramMapper.mapLessonProgramRequestToLessonProgram(lessonProgramRequest,lessons,educationTerm);

		LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);

		return ResponseMessage.<LessonProgramResponse>builder()
				.message("Lesson Program is Created")
				.httpStatus(HttpStatus.CREATED)
				.object(lessonProgramMapper.mapLessonProgramtoLessonProgramResponse(savedLessonProgram))
				.build();
	}

	//TODO add a validation for empty collection and send a meaningful response

	public Set<LessonProgramResponse> getLessonProgramByTeacher(String username){
		return lessonProgramRepository.getLessonProgramByTeachersUsername(username)
				.stream().map(lessonProgramMapper::mapLessonProgramtoLessonProgramResponse)
				.collect(Collectors.toSet());
	}


	public List<LessonProgramResponse> getAllLessonProgramList(){
		return lessonProgramRepository
				.findAll()
				.stream()
				.map(lessonProgramMapper::mapLessonProgramtoLessonProgramResponse)
				.collect(Collectors.toList());
	}

	public Page<LessonProgramResponse> getAllLessonProgramByPage(int page,int size, String sort,String type){
		Pageable pageable = pageableHelper.getPageableWithProperties(page,size,sort,type);
		return lessonProgramRepository.findAll(pageable).map(lessonProgramMapper::mapLessonProgramtoLessonProgramResponse);
	}

	public LessonProgramResponse getLessonProgramById(Long id){
		isLessonProgramExistById(id);
		return lessonProgramMapper.mapLessonProgramtoLessonProgramResponse(lessonProgramRepository.findById(id).get());
	}

	public List<LessonProgramResponse>getAllLessonProgramUnassigned(){
			return lessonProgramRepository.findByTeachers_IdNull()
					.stream()
					.map(lessonProgramMapper::mapLessonProgramtoLessonProgramResponse)
					.collect(Collectors.toList());
	}

	public List<LessonProgramResponse>getAllAssigned(){
		return lessonProgramRepository.findByTeachers_IdNotNull()
				.stream()
				.map(lessonProgramMapper::mapLessonProgramtoLessonProgramResponse)
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
				.orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE,id)));
	}

	public Set<LessonProgram> getLessonProgramById(Set<Long> lessonIdSet){

		Set<LessonProgram> lessonPrograms = lessonProgramRepository.getLessonProgramByLessonProgramIdList(lessonIdSet);

		if(lessonPrograms.isEmpty()){
			throw new BadRequestException(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE_WITHOUT_ID_INFO);
		}
		return lessonPrograms;
	}

}
