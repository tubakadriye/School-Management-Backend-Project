package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.response.ContactMessageResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("contactMessages")
@RequiredArgsConstructor
public class ContactMessageContoller {

	private final ContactMessageService contactMessageService;

	@PostMapping("/save")
	public ResponseMessage<ContactMessageResponse> save(){
		contactMessageService.
		return null;
	}



}
