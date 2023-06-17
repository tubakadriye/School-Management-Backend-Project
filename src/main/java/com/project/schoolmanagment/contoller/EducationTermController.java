package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.EducationTermRequest;
import com.project.schoolmanagment.payload.response.EducationTermResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("educationTerms")
@RequiredArgsConstructor
public class EducationTermController {

	private  final EducationTermService educationTermService;

	@PostMapping("/save")
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
	public ResponseMessage<EducationTermResponse>saveEducationTerm(@RequestBody @Valid EducationTermRequest educationTermRequest){
		return educationTermService.saveEducationTerm(educationTermRequest);
	}

}
