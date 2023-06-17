package com.project.schoolmanagment.service;

import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.request.EducationTermRequest;
import com.project.schoolmanagment.payload.response.EducationTermResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.EducationTermRepository;
import com.project.schoolmanagment.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {

	private final EducationTermRepository educationTermRepository;


	public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest){



	}

	private void checkEducationTermDate(EducationTermRequest educationTermRequest){

		if(educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
			throw new ResourceNotFoundException(Messages.)
		}
	}



}
