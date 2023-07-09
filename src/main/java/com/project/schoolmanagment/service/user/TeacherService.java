package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.service.businnes.AdvisoryTeacherService;
import com.project.schoolmanagment.service.businnes.LessonProgramService;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.payload.mappers.TeacherMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.request.ChooseLessonTeacherRequest;
import com.project.schoolmanagment.payload.request.TeacherRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.TeacherResponse;
import com.project.schoolmanagment.repository.user.TeacherRepository;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

	private final TeacherRepository teacherRepository;

	private final LessonProgramService lessonProgramService;

	private final UniquePropertyValidator uniquePropertyValidator;

	private final TeacherMapper teacherMapper;

	private final UserRoleService userRoleService;

	private final PasswordEncoder passwordEncoder;

	private final AdvisoryTeacherService advisoryTeacherService;

	private final DateTimeValidator dateTimeValidator;

	private final PageableHelper pageableHelper;

	public ResponseMessage<TeacherResponse>saveTeacher(TeacherRequest teacherRequest){
		//need to get lessonPrograms
		Set<LessonProgram> lessonProgramSet = lessonProgramService.getLessonProgramById(teacherRequest.getLessonsIdList());

		uniquePropertyValidator.checkDuplicate(teacherRequest.getUsername(),
										teacherRequest.getSsn(),
										teacherRequest.getPhoneNumber(),
										teacherRequest.getEmail());


		Teacher teacher = teacherMapper.mapTeacherRequestToTeacher(teacherRequest);
		teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
		teacher.setLessonsProgramList(lessonProgramSet);
		teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));

		Teacher savedTeacher = teacherRepository.save(teacher);
		if(teacherRequest.isAdvisorTeacher()){
			advisoryTeacherService.saveAdvisoryTeacher(teacher);
		}

		return ResponseMessage.<TeacherResponse>builder()
				.message(SuccessMessages.TEACHER_SAVE)
				.httpStatus(HttpStatus.CREATED)
				.object(teacherMapper.mapTeacherToTeacherResponse(savedTeacher))
				.build();
	}


	public List<TeacherResponse>getAllTeacher(){
		return teacherRepository.findAll()
				.stream()
				.map(teacherMapper::mapTeacherToTeacherResponse)
				.collect(Collectors.toList());
	}

	public List<TeacherResponse>getTeacherByName(String teacherName){
		return teacherRepository.getTeachersByNameContaining(teacherName)
				.stream()
				.map(teacherMapper::mapTeacherToTeacherResponse)
				.collect(Collectors.toList());
	}

	public ResponseMessage deleteTeacherById(Long id){
		isTeacherExist(id);
		teacherRepository.deleteById(id);

		return ResponseMessage.builder()
				.message(SuccessMessages.TEACHER_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	private Teacher isTeacherExist(Long id){
		return teacherRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));
	}

	public ResponseMessage<TeacherResponse>getTeacherById(Long id){

		return ResponseMessage.<TeacherResponse>builder()
				.object(teacherMapper.mapTeacherToTeacherResponse(isTeacherExist(id)))
				.message(SuccessMessages.TEACHER_FOUND)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public Page<TeacherResponse> findTeacherByPage(int page,int size,String sort,String type){
		Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
		return teacherRepository.findAll(pageable).map(teacherMapper::mapTeacherToTeacherResponse);
	}

	public ResponseMessage<TeacherResponse>updateTeacher(TeacherRequest teacherRequest, Long userId){
		Teacher teacher = isTeacherExist(userId);
		Set<LessonProgram>lessonPrograms = lessonProgramService.getLessonProgramById(teacherRequest.getLessonsIdList());
		uniquePropertyValidator.checkUniqueProperties(teacher,teacherRequest);
		Teacher updatedTeacher = teacherMapper.mapTeacherRequestToUpdatedTeacher(teacherRequest,userId);
		//props. that does not exist in mappers
		updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
		updatedTeacher.setLessonsProgramList(lessonPrograms);
		updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

		Teacher savedTeacher = teacherRepository.save(updatedTeacher);
		advisoryTeacherService.updateAdvisoryTeacher(teacherRequest.isAdvisorTeacher(),savedTeacher);

		return ResponseMessage.<TeacherResponse>builder()
				.object(teacherMapper.mapTeacherToTeacherResponse(savedTeacher))
				.message(SuccessMessages.TEACHER_UPDATE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public ResponseMessage<TeacherResponse>chooseLesson (ChooseLessonTeacherRequest chooseLessonTeacherRequest){
		Teacher teacher = isTeacherExist(chooseLessonTeacherRequest.getTeacherId());

		Set<LessonProgram>lessonPrograms = lessonProgramService.getLessonProgramById(chooseLessonTeacherRequest.getLessonProgramId());

		Set<LessonProgram>teachersLessonProgram = teacher.getLessonsProgramList();

		dateTimeValidator.checkLessonPrograms(teachersLessonProgram ,lessonPrograms);
		teachersLessonProgram.addAll(lessonPrograms);
		teacher.setLessonsProgramList(teachersLessonProgram);
		Teacher updatedTeacher = teacherRepository.save(teacher);

		return ResponseMessage.<TeacherResponse>builder()
				.message(SuccessMessages.LESSON_PROGRAM_ADD_TO_TEACHER)
				.httpStatus(HttpStatus.CREATED)
				.object(teacherMapper.mapTeacherToTeacherResponse(updatedTeacher))
				.build();

	}

	public Teacher getTeacherByUsername(String username){
		if(!teacherRepository.existsByUsername(username)){
			throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE);
		}
		return teacherRepository.getTeachersByUsername(username);
	}










}
