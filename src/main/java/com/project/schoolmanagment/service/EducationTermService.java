package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.EducationTerm;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.EducationTermDto;
import com.project.schoolmanagment.payload.request.EducationTermRequest;
import com.project.schoolmanagment.payload.response.EducationTermResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.EducationTermRepository;
import com.project.schoolmanagment.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {

	private final EducationTermRepository educationTermRepository;

	private final EducationTermDto educationTermDto;


	public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest){

		validateEducationTermDates(educationTermRequest);

		EducationTerm savedEducationTerm = educationTermRepository.save(educationTermDto.mapEducationTermRequestToEducationTerm(educationTermRequest));

		return ResponseMessage.<EducationTermResponse>builder()
				.message("Education Term Saved")
				.object(educationTermDto.mapEducationTermToEducationTermResponse(savedEducationTerm))
				.httpStatus(HttpStatus.CREATED)
				.build();

	}

	private void validateEducationTermDates(EducationTermRequest educationTermRequest){

		//TODO another requirement can be needed for validating ->  registration > end
		// check the dates also for TODAY

		// registration > start
		if(educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
			throw new ResourceNotFoundException(Messages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
		}

		// end > start
		if(educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())){
			throw new ResourceNotFoundException(Messages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
		}

		// if any education term exist in these year with this term
		if(educationTermRepository.existsByTermAndYear(educationTermRequest.getTerm(),educationTermRequest.getStartDate().getYear())){
			throw new ResourceNotFoundException(Messages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
		}


	}



}
