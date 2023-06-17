package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("educationTerms")
@RequiredArgsConstructor
public class EducationTermController {

	EducationTermService educationTermService;

	public ResponseMessage<>saveEducationTerm(){

	}

}
