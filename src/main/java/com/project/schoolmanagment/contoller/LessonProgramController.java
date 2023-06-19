package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/lessonPrograms")
@RequiredArgsConstructor
public class LessonProgramController {


	private final LessonProgramService lessonProgramService;


	@PostMapping("/save")
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
	public ResponseMessage<LessonProgramResponse>saveLessonProgram(@RequestBody @Valid LessonProgramRequest lessonProgramRequest){
		return lessonProgramService.saveLessonProgram(lessonProgramRequest);

	}




}
