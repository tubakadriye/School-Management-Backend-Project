package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.ContactMessage;
import com.project.schoolmanagment.payload.request.ContactMessageRequest;
import com.project.schoolmanagment.payload.response.ContactMessageResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

	private final ContactMessageRepository contactMessageRepository;

	public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest){

		ContactMessage contactMessage = createContactMessage(contactMessageRequest);
		return null;
	}

	//TODO please check builder design pattern
	private ContactMessage createContactMessage(ContactMessageRequest contactMessageRequest){
		return ContactMessage.builder()
				.name(contactMessageRequest.getName())
				.subject(contactMessageRequest.getSubject())
				.message(contactMessageRequest.getMessage())
				.email(contactMessageRequest.getEmail())
				.date(LocalDate.now())
				.build();
	}






}
