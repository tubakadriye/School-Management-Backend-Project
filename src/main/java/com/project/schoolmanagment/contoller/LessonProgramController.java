package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lessonPrograms")
@RequiredArgsConstructor
public class LessonProgramController {


	@PostMapping("/save")
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
	public ResponseMessage<LessonProgramResponse>saveLessonProgram(@RequestBody LessonProgramRequest lessonProgramRequest){

	}




}
