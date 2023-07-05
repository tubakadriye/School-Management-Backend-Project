package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.MeetRequest;
import com.project.schoolmanagment.payload.response.MeetResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("meet")
@RequiredArgsConstructor
public class MeetController {

	private final MeetService meetService;

	@PreAuthorize("hasAnyAuthority('TEACHER')")
	@PostMapping("/save")
	public ResponseMessage<MeetResponse>saveMeet(HttpServletRequest httpServletRequest,
	                                             @RequestBody @Valid MeetRequest meetRequest){
		String username = (String) httpServletRequest.getAttribute("username");
		return meetService.saveMeet(username,meetRequest);
	}

	@PreAuthorize("hasAnyAuthority( 'ADMIN')")
	@GetMapping("/getAll")
	public List<MeetResponse> getAll(){
		return meetService.getAll();
	}

	@PreAuthorize("hasAnyAuthority( 'ADMIN')")
	@GetMapping("/getMeetById/{meetId}")
	public ResponseMessage<MeetResponse> getMeetById(@PathVariable Long meetId){
		return meetService.getMeetById(meetId);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER','ADMIN' )")
	@DeleteMapping("/delete/{meetId}")
	public ResponseMessage delete(@PathVariable Long meetId){
		return meetService.delete(meetId);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER','ADMIN' )")
	@PutMapping("/update/{meetId}")
	public ResponseMessage<MeetResponse>updateMeet(@RequestBody @Valid UpdateMeetRequest updateMeetRequest,
	                                               @PathVariable Long meetId){
		return meetService.updateMeet(updateMeetRequest,meetId);
	}




}
