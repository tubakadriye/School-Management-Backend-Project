package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.Dean;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.DeanMapper;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.DeanRequest;
import com.project.schoolmanagment.payload.response.DeanResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.DeanRepository;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeanService {


	private final UniquePropertyValidator uniquePropertyValidator;

	private final DeanMapper deanMapper;

	private final UserRoleService userRoleService;

	private final PasswordEncoder passwordEncoder;

	private final DeanRepository deanRepository;

	private final PageableHelper pageableHelper;

	public ResponseMessage<DeanResponse> save(DeanRequest deanRequest) {
		uniquePropertyValidator.checkDuplicate(deanRequest.getUsername(), deanRequest.getSsn(), deanRequest.getPhoneNumber());
		Dean dean = deanMapper.mapDeanRequestToDean(deanRequest);
		dean.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
		dean.setPassword(passwordEncoder.encode(dean.getPassword()));

		Dean savedDean = deanRepository.save(dean);

		return ResponseMessage.<DeanResponse>builder()
				.message(SuccessMessages.DEAN_SAVE)
				.httpStatus(HttpStatus.CREATED)
				.object(deanMapper.mapDeanToDeanResponse(savedDean))
				.build();
	}

	public ResponseMessage<DeanResponse> update(DeanRequest deanRequest, Long deanId) {
		Dean dean = isDeanExist(deanId);

			//we are preventing the user to change the username + ssn + phoneNumber
		uniquePropertyValidator.checkUniqueProperties(dean, deanRequest);

		Dean updatedDean = deanMapper.mapDeanRequestToUpdatedDean(deanRequest, deanId);
		updatedDean.setPassword(passwordEncoder.encode(deanRequest.getPassword()));
		Dean savedDean = deanRepository.save(updatedDean);

		return ResponseMessage.<DeanResponse>builder()
				.message(SuccessMessages.DEAN_UPDATE)
				.httpStatus(HttpStatus.OK)
				.object(deanMapper.mapDeanToDeanResponse(savedDean))
				.build();

	}

	private Dean isDeanExist(Long deanId) {
		return  deanRepository.findById(deanId)
				.orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, deanId)));
	}


	public ResponseMessage deleteDeanById(Long deanId) {

		isDeanExist(deanId);

		deanRepository.deleteById(deanId);

		return ResponseMessage.builder()
				.message(SuccessMessages.DEAN_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public ResponseMessage<DeanResponse>getDeanById(Long deanId){
		return ResponseMessage.<DeanResponse>builder()
				.message(SuccessMessages.DEAN_FOUND)
				.httpStatus(HttpStatus.OK)
				.object(deanMapper.mapDeanToDeanResponse(isDeanExist(deanId)))
				.build();
	}

	public List<DeanResponse> getAllDeans(){
		return deanRepository.findAll()
				.stream()
				.map(deanMapper::mapDeanToDeanResponse)
				.collect(Collectors.toList());
	}


	public Page<DeanResponse> getAllDeansByPage(int page,int size,String sort,String type){

		Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

		return deanRepository.findAll(pageable).map(deanMapper::mapDeanToDeanResponse);
	}



}
