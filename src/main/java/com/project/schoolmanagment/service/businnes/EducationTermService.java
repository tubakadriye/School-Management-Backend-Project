package com.project.schoolmanagment.service.businnes;

import com.project.schoolmanagment.entity.concretes.EducationTerm;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.EducationTermMapper;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.EducationTermRequest;
import com.project.schoolmanagment.payload.response.EducationTermResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.EducationTermRepository;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {

	private final EducationTermRepository educationTermRepository;

	private final EducationTermMapper educationTermMapper;

	private final PageableHelper pageableHelper;


	public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest){
		validateEducationTermDates(educationTermRequest);
		EducationTerm savedEducationTerm = educationTermRepository.save(educationTermMapper.mapEducationTermRequestToEducationTerm(educationTermRequest));
		return ResponseMessage.<EducationTermResponse>builder()
													.message(SuccessMessages.EDUCATION_TERM_SAVE)
													.object(educationTermMapper.mapEducationTermToEducationTermResponse(savedEducationTerm))
													.httpStatus(HttpStatus.CREATED)
													.build();
	}

	public List<EducationTermResponse> getAllEducationTerms(){
		return educationTermRepository.findAll()
										.stream()
										.map(educationTermMapper::mapEducationTermToEducationTermResponse)
										.collect(Collectors.toList());
	}


	public EducationTermResponse getEducationTermResponseById(Long id){
		EducationTerm term = isEducationTermExist(id);
		return educationTermMapper.mapEducationTermToEducationTermResponse(term);
	}

	public EducationTerm getEducationTermById(Long id){
		return isEducationTermExist(id);
	}


	public ResponseMessage deleteEducationTermById(Long id){
		isEducationTermExist(id);
		educationTermRepository.deleteById(id);
		return ResponseMessage.builder()
				.message(SuccessMessages.EDUCATION_TERM_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}


	public Page<EducationTermResponse> getAllEducationTermsByPage (int page,int size,String sort,String type){
		Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

		return educationTermRepository.findAll(pageable).map(educationTermMapper::mapEducationTermToEducationTermResponse);
	}

	public ResponseMessage<EducationTermResponse>updateEducationTerm(Long id,EducationTermRequest educationTermRequest){

		isEducationTermExist(id);

		validateEducationTermDatesForUpdate(educationTermRequest);

		EducationTerm educationTermUpdated = educationTermRepository.save(educationTermMapper.mapEducationTermRequestToUpdatedEducationTerm(id,educationTermRequest));

		return ResponseMessage.<EducationTermResponse>builder()
				.message(SuccessMessages.EDUCATION_TERM_UPDATE)
				.httpStatus(HttpStatus.OK)
				.object(educationTermMapper.mapEducationTermToEducationTermResponse(educationTermUpdated))
				.build();

	}

	private EducationTerm isEducationTermExist(Long id){
		EducationTerm term = educationTermRepository.findByIdEquals(id);
		if(term==null){
			throw new ResourceNotFoundException(String.format(ErrorMessages.EDUCATION_TERM_NOT_FOUND_MESSAGE,id));
		} else {
			return term;
		}
	}



	private void validateEducationTermDates(EducationTermRequest educationTermRequest){

		//TODO another requirement can be needed for validating ->  registration > end
		// check the dates also for TODAY
		validateEducationTermDatesForUpdate(educationTermRequest);
		// we need one more validatetion in addition to validation above

		// if any education term exist in these year with this term
		if(educationTermRepository.existsByTermAndYear(educationTermRequest.getTerm(),educationTermRequest.getStartDate().getYear())){
			throw new ResourceNotFoundException(ErrorMessages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
		}


	}

	private void validateEducationTermDatesForUpdate(EducationTermRequest educationTermRequest){
		//TODO another requirement can be needed for validating ->  registration > end
		// check the dates also for TODAY
		// registration > start
		if(educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
			throw new ResourceNotFoundException(ErrorMessages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
		}
		// end > start
		if(educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())){
			throw new ResourceNotFoundException(ErrorMessages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
		}
	}








}
