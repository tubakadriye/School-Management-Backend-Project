package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.payload.request.StudentRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.StudentResponse;
import com.project.schoolmanagment.repository.StudentRepository;
import com.project.schoolmanagment.utils.ServiceHelpers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final AdvisoryTeacherService advisoryTeacherService;

	private final StudentRepository studentRepository;

	private final ServiceHelpers serviceHelpers;


	public ResponseMessage<StudentResponse>saveStudent(StudentRequest studentRequest){
		//we need to find the advisory teacher of this student
		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisoryTeacherById(studentRequest.getAdvisorTeacherId());,
		//we need to check duplication
		//correct order since we have varargs
		serviceHelpers.checkDuplicate(studentRequest.getUsername()
									,studentRequest.getSsn()
									,studentRequest.getPhoneNumber()
									,studentRequest.getEmail());


	}
}
