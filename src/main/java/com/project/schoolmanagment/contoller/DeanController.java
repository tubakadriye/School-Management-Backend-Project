package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.DeanRequest;
import com.project.schoolmanagment.payload.response.DeanResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("dean")
public class DeanController {

	private final DeanService deanService;

	@PostMapping("/save")
	public ResponseMessage<DeanResponse> save (@RequestBody @Valid DeanRequest deanRequest){
		return null;
	}







}
