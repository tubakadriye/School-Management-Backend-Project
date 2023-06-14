package com.project.schoolmanagment.service;

import com.project.schoolmanagment.payload.request.DeanRequest;
import com.project.schoolmanagment.payload.response.DeanResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeanService {


	AdminService adminService;

	public ResponseMessage<DeanResponse>save(DeanRequest deanRequest){
		adminService.checkDuplicate();
		return null;
	}



}
