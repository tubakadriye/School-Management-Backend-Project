package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.ViceDean;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.ViceDeanMapper;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.ViceDeanRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.ViceDeanResponse;
import com.project.schoolmanagment.repository.ViceDeanRepository;
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
public class ViceDeanService {

	private final ViceDeanRepository viceDeanRepository;

	public final UniquePropertyValidator uniquePropertyValidator;

	public final ViceDeanMapper viceDeanMapper;

	public final PasswordEncoder passwordEncoder;

	public final UserRoleService userRoleService;

	private final PageableHelper pageableHelper;

	public ResponseMessage<ViceDeanResponse> saveViceDean(ViceDeanRequest viceDeanRequest) {

		uniquePropertyValidator.checkDuplicate(viceDeanRequest.getUsername(),
				viceDeanRequest.getSsn(),
				viceDeanRequest.getPhoneNumber());

		ViceDean viceDean = viceDeanMapper.mapViceDeanRequestToViceDean(viceDeanRequest);
		viceDean.setPassword(passwordEncoder.encode(viceDeanRequest.getPassword()));
		viceDean.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));

		ViceDean savedViceDean = viceDeanRepository.save(viceDean);

		return ResponseMessage.<ViceDeanResponse>builder()
				.message(SuccessMessages.VICE_DEAN_SAVE)
				.httpStatus(HttpStatus.CREATED)
				.object(viceDeanMapper.mapViceDeanToViceDeanResponse(savedViceDean))
				.build();
	}

	private Optional<ViceDean> isViceDeanExist(Long viceDeanId) {
		Optional<ViceDean> viceDean = viceDeanRepository.findById(viceDeanId);
		if (!viceDeanRepository.findById(viceDeanId).isPresent()) {
			throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, viceDeanId));
		} else {
			return viceDean;
		}
	}

	public ResponseMessage<?> deleteViceDeanByUserId(Long viceDeanId) {

		viceDeanRepository.deleteById(viceDeanId);

		return ResponseMessage.builder()
				.message(SuccessMessages.VICE_DEAN_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}


	public ResponseMessage<ViceDeanResponse>getViceDeanByViceDeanId(Long viceDeanId){
		return ResponseMessage.<ViceDeanResponse>builder()
				.message(SuccessMessages.VICE_DEAN_FOUND)
				.httpStatus(HttpStatus.OK)
				.object(viceDeanMapper.mapViceDeanToViceDeanResponse(isViceDeanExist(viceDeanId).get()))
				.build();
	}

	public ResponseMessage<ViceDeanResponse> updateViceDean(ViceDeanRequest viceDeanRequest, Long viceDeanId){
		Optional<ViceDean>viceDean = isViceDeanExist(viceDeanId);

		uniquePropertyValidator.checkUniqueProperties(viceDean.get(),viceDeanRequest);

		ViceDean updatedViceDean = viceDeanMapper.mapViceDeanRequestToUpdatedViceDean(viceDeanRequest,viceDeanId);
		updatedViceDean.setPassword(passwordEncoder.encode(viceDeanRequest.getPassword()));
		ViceDean savedViceDean = viceDeanRepository.save(updatedViceDean);

		return ResponseMessage.<ViceDeanResponse>builder()
				.message(SuccessMessages.VICE_DEAN_UPDATE)
				.httpStatus(HttpStatus.OK)
				.object(viceDeanMapper.mapViceDeanToViceDeanResponse(savedViceDean))
				.build();
	}

	//TODO learn more about stream API
	public List<ViceDeanResponse> getAllViceDeans(){
		return viceDeanRepository.findAll()
				.stream()
				.map(viceDeanMapper::mapViceDeanToViceDeanResponse)
				.collect(Collectors.toList());
	}

	public Page<ViceDeanResponse> getAllViceDeansByPage(int page, int size, String sort, String type){

		Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

		return viceDeanRepository.findAll(pageable).map(viceDeanMapper::mapViceDeanToViceDeanResponse);
	}



}
