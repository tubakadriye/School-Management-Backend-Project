package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.Student;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.service.businnes.AdvisoryTeacherService;
import com.project.schoolmanagment.service.businnes.LessonProgramService;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.payload.mappers.StudentMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.request.ChooseLessonProgramWithId;
import com.project.schoolmanagment.payload.request.StudentRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.StudentResponse;
import com.project.schoolmanagment.repository.user.StudentRepository;
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
public class StudentService {

	private final AdvisoryTeacherService advisoryTeacherService;

	private final StudentRepository studentRepository;

	private final UniquePropertyValidator uniquePropertyValidator;

	private final StudentMapper studentMapper;

	private final PasswordEncoder passwordEncoder;

	private final UserRoleService userRoleService;

	private final LessonProgramService lessonProgramService;

	private final DateTimeValidator dateTimeValidator;

	private final PageableHelper pageableHelper;


	public ResponseMessage<StudentResponse>saveStudent(StudentRequest studentRequest){
		//we need to find the advisory teacher of this student
		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisoryTeacherById(studentRequest.getAdvisorTeacherId());
		//we need to check duplication
		//correct order since we have varargs
		uniquePropertyValidator.checkDuplicate(studentRequest.getUsername()
									,studentRequest.getSsn()
									,studentRequest.getPhoneNumber()
									,studentRequest.getEmail());

		Student student = studentMapper.mapStudentRequestToStudent(studentRequest);
		student.setAdvisoryTeacher(advisoryTeacher);
		student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
		student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
		student.setActive(true);
		student.setStudentNumber(getLastNumber());

		return ResponseMessage.<StudentResponse>builder()
				.object(studentMapper.mapStudentToStudentResponse(studentRepository.save(student)))
				.message(SuccessMessages.STUDENT_SAVE)
				.build();
	}

	private int getLastNumber(){
		//we are just checking the database if we have any student
		if(!studentRepository.findStudent()){
			//first student
			return 1000;
		}
		return studentRepository.getMaxStudentNumber() + 1;
	}

	public ResponseMessage changeStatus(Long studentId,boolean status){
		Student student = isStudentsExist(studentId);
		student.setActive(status);
		studentRepository.save(student);
		return ResponseMessage.builder()
				.message("Student is " + (status ? "active" : "passive"))
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public Student isStudentsExist(Long studentId){
		return studentRepository
				.findById(studentId)
				.orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,studentId)));
	}

	public Student isStudentsExistByUsername(String username){
		Student student =  studentRepository.findByUsernameEquals(username);
		if(student.getId()==null){
			throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE);
		}
		return student;
	}

	public List<StudentResponse> getAllStudents(){
		return studentRepository.findAll()
				.stream()
				.map(studentMapper::mapStudentToStudentResponse)
				.collect(Collectors.toList());
	}

	public ResponseMessage<StudentResponse>updateStudent(Long studentId, StudentRequest studentRequest){
		Student student = isStudentsExist(studentId);

		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisoryTeacherById(studentRequest.getAdvisorTeacherId());

		uniquePropertyValidator.checkUniqueProperties(student,studentRequest);

		Student studentForUpdate = studentMapper.mapStudentRequestToUpdatedStudent(studentRequest,studentId);
		studentForUpdate.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
		studentForUpdate.setAdvisoryTeacher(advisoryTeacher);
		studentForUpdate.setStudentNumber(student.getStudentNumber());
		studentForUpdate.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
		studentForUpdate.setActive(true);
		studentRepository.save(studentForUpdate);
		return ResponseMessage.<StudentResponse>builder()
				.object(studentMapper.mapStudentToStudentResponse(studentRepository.save(studentForUpdate)))
				.message(SuccessMessages.STUDENT_UPDATE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public ResponseMessage deleteStudentById(Long id){
		isStudentsExist(id);
		studentRepository.deleteById(id);
		return ResponseMessage.builder()
				.message(SuccessMessages.STUDENT_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public List<StudentResponse> findStudentsByName(String studentName){
		return studentRepository.getStudentByNameContaining(studentName)
				.stream()
				.map(studentMapper::mapStudentToStudentResponse)
				.collect(Collectors.toList());
	}

	public Page<StudentResponse>search(int page,int size,String sort,String type){
		Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
		return studentRepository.findAll(pageable)
				.map(studentMapper::mapStudentToStudentResponse);
	}

	public Student getStudentById(Long id){
		return isStudentsExist(id);
	}

	public List<StudentResponse>getAllByAdvisoryUsername(String advisoryTeacherUserName){
		return studentRepository.getStudentByAdvisoryTeacher_Username(advisoryTeacherUserName)
				.stream()
				.map(studentMapper::mapStudentToStudentResponse)
				.collect(Collectors.toList());
	}

	public ResponseMessage<StudentResponse>chooseLesson(String username,
	                                                    ChooseLessonProgramWithId chooseLessonProgramWithId){
		Student student = isStudentsExistByUsername(username);
		Set<LessonProgram> lessonProgramSet = lessonProgramService.getLessonProgramById(chooseLessonProgramWithId.getLessonProgramId());
		Set<LessonProgram>studentCurrentLessonProgram = student.getLessonsProgramList();
		dateTimeValidator.checkLessonPrograms(lessonProgramSet,studentCurrentLessonProgram);
		studentCurrentLessonProgram.addAll(lessonProgramSet);
		//we are updating the lesson program of the student
		student.setLessonsProgramList(studentCurrentLessonProgram);

		Student savedStudent = studentRepository.save(student);

		return ResponseMessage.<StudentResponse>builder()
				.message(SuccessMessages.LESSON_PROGRAM_ADD_TO_STUDENT)
				.object(studentMapper.mapStudentToStudentResponse(savedStudent))
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public List<Student>getStudentById(Long[]studentIds){
		return studentRepository.findByIdsEquals(studentIds);
	}




}
