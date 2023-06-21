package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.mappers.TeacherDto;
import com.project.schoolmanagment.payload.request.TeacherRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.TeacherResponse;
import com.project.schoolmanagment.repository.TeacherRepository;
import com.project.schoolmanagment.utils.Messages;
import com.project.schoolmanagment.utils.ServiceHelpers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherService {

	private final TeacherRepository teacherRepository;

	private final LessonProgramService lessonProgramService;

	private final ServiceHelpers serviceHelpers;

	private final TeacherDto teacherDto;

	private final UserRoleService userRoleService;

	private final PasswordEncoder passwordEncoder;

	private final AdvisoryTeacherService advisoryTeacherService;


	public ResponseMessage<TeacherResponse>saveTeacher(TeacherRequest teacherRequest){
		//need to get lessonPrograms
		Set<LessonProgram> lessonProgramSet = lessonProgramService.getLessonProgramById(teacherRequest.getLessonsIdList());

		serviceHelpers.checkDuplicate(teacherRequest.getUsername(),
										teacherRequest.getSsn(),
										teacherRequest.getPhoneNumber(),
										teacherRequest.getEmail());


		Teacher teacher = teacherDto.mapTeacherRequestToTeacher(teacherRequest);
		teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
		teacher.setLessonsProgramList(lessonProgramSet);
		teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));

		Teacher savedTeacher = teacherRepository.save(teacher);
		if(teacherRequest.isAdvisorTeacher()){
			advisoryTeacherService.saveAdvisoryTeacher(teacher);
		}

		return ResponseMessage.<TeacherResponse>builder()
				.message("Teacher saved successfully")
				.httpStatus(HttpStatus.CREATED)
				.object(teacherDto.mapTeacherToTeacherResponse(savedTeacher))
				.build();
	}









}
