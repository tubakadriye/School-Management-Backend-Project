package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.Dean;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.mappers.DeanDto;
import com.project.schoolmanagment.payload.request.DeanRequest;
import com.project.schoolmanagment.payload.response.DeanResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.DeanRepository;
import com.project.schoolmanagment.utils.FieldControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeanService {


	private final FieldControl fieldControl;

	private final DeanDto deanDto;

	private final UserRoleService userRoleService;

	private final PasswordEncoder passwordEncoder;

	private final DeanRepository deanRepository;

	//TODO use mapsturct in your 3. repository
	public ResponseMessage<DeanResponse>save(DeanRequest deanRequest){
		fieldControl.checkDuplicate(deanRequest.getUsername(),deanRequest.getSsn(),deanRequest.getPhoneNumber());
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






}
