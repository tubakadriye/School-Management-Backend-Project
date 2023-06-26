package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.StudentInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {


	@PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
	@PostMapping("/save")
	public ResponseMessage<StudentInfoResponse>saveStudentInfo(){

	}

}
