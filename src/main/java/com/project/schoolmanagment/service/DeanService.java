package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.Dean;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.DeanDto;
import com.project.schoolmanagment.payload.request.DeanRequest;
import com.project.schoolmanagment.payload.response.DeanResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.DeanRepository;
import com.project.schoolmanagment.utils.CheckParameterUpdateMethod;
import com.project.schoolmanagment.utils.FieldControl;
import com.project.schoolmanagment.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeanService {


	private final FieldControl fieldControl;

	private final DeanDto deanDto;

	private final UserRoleService userRoleService;

	private final PasswordEncoder passwordEncoder;

	private final DeanRepository deanRepository;

	//TODO use mapsturct in your 3. repository
	public ResponseMessage<DeanResponse> save(DeanRequest deanRequest) {
		fieldControl.checkDuplicate(deanRequest.getUsername(), deanRequest.getSsn(), deanRequest.getPhoneNumber());
		Dean dean = deanDto.mapDeanRequestToDean(deanRequest);
		dean.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
		dean.setPassword(passwordEncoder.encode(dean.getPassword()));

		Dean savedDean = deanRepository.save(dean);

		return ResponseMessage.<DeanResponse>builder()
				.message("Dean Saved")
				.httpStatus(HttpStatus.CREATED)
				.object(deanDto.mapDeanToDeanResponse(savedDean))
				.build();
	}

	public ResponseMessage<DeanResponse> update(DeanRequest deanRequest, Long deanId) {
		Optional<Dean> dean = isDeanExist(deanId);

			//we are preventing the user to change the username + ssn + phoneNumber
		if (!CheckParameterUpdateMethod.checkUniqueProperties(dean.get(), deanRequest)) {   //DEAN -> DEAN2
			fieldControl.checkDuplicate(deanRequest.getUsername(),
										deanRequest.getSsn(),
										deanRequest.getPhoneNumber());
		}

		Dean updatedDean = deanDto.mapDeanRequestToUpdatedDean(deanRequest, deanId);
		updatedDean.setPassword(passwordEncoder.encode(deanRequest.getPassword()));
		Dean savedDean = deanRepository.save(updatedDean);

		return ResponseMessage.<DeanResponse>builder()
				.message("Dean Updated Successfully")
				.httpStatus(HttpStatus.OK)
				.object(deanDto.mapDeanToDeanResponse(savedDean))
				.build();

	}

	private Optional<Dean> isDeanExist(Long deanId) {
		Optional<Dean> dean = deanRepository.findById(deanId);

//		Optional<Dean> dean = deanRepository.findById(deanId)
//				.orElseThrow(()-> new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER_MESSAGE, deanId)));

		if (!deanRepository.findById(deanId).isPresent()) {
			throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER_MESSAGE, deanId));
		} else {
			return dean;
		}
	}


	public ResponseMessage<?> deleteDeanById(Long deanId) {

		isDeanExist(deanId);

		deanRepository.deleteById(deanId);

		return ResponseMessage.builder()
				.message("Dean deleted")
				.httpStatus(HttpStatus.OK)
				.build();
	}


}
