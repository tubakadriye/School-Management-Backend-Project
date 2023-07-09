package com.project.schoolmanagment.service.businnes;

import com.project.schoolmanagment.entity.concretes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.Lesson;
import com.project.schoolmanagment.entity.concretes.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.LessonProgramMapper;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.businnes.LessonProgramRepository;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.TimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

	private final PageableHelper pageableHelper;


	public ResponseMessage<LessonProgramResponse>saveLessonProgram(LessonProgramRequest lessonProgramRequest){

		Set<Lesson> lessons = lessonService.getLessonByLessonIdSet(lessonProgramRequest.getLessonIdList());

		EducationTerm educationTerm = educationTermService.getEducationTermById(lessonProgramRequest.getEducationTermId());

		if(lessons.isEmpty()){
			throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LESSON_IN_LIST);
		}

		TimeValidator.checkTimeWithException(lessonProgramRequest.getStartTime(),lessonProgramRequest.getStopTime());

		LessonProgram lessonProgram = lessonProgramMapper.mapLessonProgramRequestToLessonProgram(lessonProgramRequest,lessons,educationTerm);

		LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);

		return ResponseMessage.<LessonProgramResponse>builder()
				.message(SuccessMessages.LESSON_PROGRAM_SAVE)
				.httpStatus(HttpStatus.CREATED)
				.object(lessonProgramMapper.mapLessonProgramtoLessonProgramResponse(savedLessonProgram))
				.build();
	}

	//TODO add a validation for empty collection and send a meaningful response

	public Set<LessonProgramResponse> getLessonProgramByTeacher(HttpServletRequest httpServletRequest){
		String userName = (String) httpServletRequest.getAttribute("username");
		return lessonProgramRepository.getLessonProgramByTeachersUsername(userName)
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
		return lessonProgramMapper.mapLessonProgramtoLessonProgramResponse(isLessonProgramExistById(id));
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
				.message(SuccessMessages.LESSON_PROGRAM_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	private LessonProgram isLessonProgramExistById(Long id){
		return lessonProgramRepository.findById(id)
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
