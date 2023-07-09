package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.payload.response.AdvisorTeacherResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.businnes.AdvisoryTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/advisorTeacher")
@RestController
@RequiredArgsConstructor
public class AdvisoryTeacherController {

	private final AdvisoryTeacherService advisoryTeacherService;

	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTAN_MANAGER')")
	@GetMapping("/getAll")
	public List<AdvisorTeacherResponse> getAllAdvisorTeacher(){
		return advisoryTeacherService.getAll();
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
	@GetMapping("/search")
	public Page<AdvisorTeacherResponse> getAllAdvisorTeacherByPage(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "size") int size,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "type") String type
	){
		return advisoryTeacherService.getAllAdvisorTeacherByPage(page,size,sort,type);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
	@DeleteMapping("/delete/{id}")
	public ResponseMessage<AdvisoryTeacher> deleteAdvisorTeacher(@PathVariable Long id){
		return advisoryTeacherService.deleteAdvisorTeacherById(id);
	}



}
