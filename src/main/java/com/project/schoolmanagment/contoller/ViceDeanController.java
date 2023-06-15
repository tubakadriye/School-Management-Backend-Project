package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.ViceDeanRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.response.ViceDeanResponse;
import com.project.schoolmanagment.service.ViceDeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("vicedean")
@RequiredArgsConstructor
public class ViceDeanController {


	private final ViceDeanService viceDeanService;

	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
	@PostMapping("/save")
	public ResponseMessage<ViceDeanResponse> saveViceDean(@RequestBody @Valid ViceDeanRequest viceDeanRequest){
		return viceDeanService.saveViceDean(viceDeanRequest);
	}



}
