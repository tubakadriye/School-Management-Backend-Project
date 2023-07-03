package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.UpdateStudentInfoRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.StudentInfoResponse;
import com.project.schoolmanagment.service.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {

	private final StudentInfoService studentInfoService;


	@PreAuthorize("hasAnyAuthority('TEACHER')")
	@PostMapping("/save")
	public ResponseMessage<StudentInfoResponse>saveStudentInfo(HttpServletRequest httpServletRequest,
	                                                           @RequestBody @Valid StudentInfoRequest studentInfoRequest){
			String username = (String) httpServletRequest.getAttribute("username");
			return studentInfoService.saveStudentInfo(username,studentInfoRequest);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
	@DeleteMapping("/delete/{studentInfoId}")
	public ResponseMessage delete (@PathVariable Long studentInfoId){
		return studentInfoService.deleteStudentInfo(studentInfoId);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
	@GetMapping("/search")
	public Page<StudentInfoResponse> search(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "size") int size,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "type") String type
	) {
		return  studentInfoService.search(page,size,sort,type);
	}
	@PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
	@PutMapping("/update/{studentInfo}")
	public ResponseMessage<StudentInfoResponse>update(@RequestBody @Valid UpdateStudentInfoRequest studentInfoRequest,
	                                                  @PathVariable Long studentInfo){
		return studentInfoService.update(studentInfoRequest,studentInfo);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER')")
	@GetMapping("/getAllForTeacher")
	public ResponseEntity<Page<StudentInfoResponse>> getAllForTeacher(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "size") int size
	){
		return studentInfoService.getAllForTeacher(httpServletRequest, page, size);
	}

}
