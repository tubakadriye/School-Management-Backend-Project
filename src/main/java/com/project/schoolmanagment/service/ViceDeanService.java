package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.Dean;
import com.project.schoolmanagment.entity.concretes.ViceDean;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.mappers.ViceDeanDto;
import com.project.schoolmanagment.payload.request.ViceDeanRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.ViceDeanResponse;
import com.project.schoolmanagment.repository.ViceDeanRepository;
import com.project.schoolmanagment.utils.FieldControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViceDeanService {

	private final ViceDeanRepository viceDeanRepository;

	public final FieldControl fieldControl;

	public final ViceDeanDto viceDeanDto;

	public final PasswordEncoder passwordEncoder;

	public final UserRoleService userRoleService;


	public ResponseMessage<ViceDeanResponse> saveViceDean(ViceDeanRequest viceDeanRequest){

		fieldControl.checkDuplicate(viceDeanRequest.getUsername(),
				viceDeanRequest.getSsn(),
				viceDeanRequest.getPhoneNumber());

		ViceDean viceDean = viceDeanDto.mapViceDeanRequestToViceDean(viceDeanRequest);
		viceDean.setPassword(passwordEncoder.encode(viceDeanRequest.getPassword()));
		viceDean.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));

		ViceDean savedViceDean =  viceDeanRepository.save(viceDean);

		return ResponseMessage.<ViceDeanResponse>builder()
				.message("Vice Dean Saved")
				.httpStatus(HttpStatus.CREATED)
				.object(viceDeanDto.mapViceDeanToViceDeanResponse(savedViceDean))
				.build();
	}




}
