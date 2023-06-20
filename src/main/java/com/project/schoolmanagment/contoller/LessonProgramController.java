package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

	@GetMapping("/getAll")
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
	public List<LessonProgramResponse> getAllLessonProgramByList(){
		return lessonProgramService.getAllLessonProgramList();
	}

	@GetMapping("getById/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
	public LessonProgramResponse getLessonProgramById(@PathVariable Long id){
		return lessonProgramService.getLessonProgramById(id);
	}

	@GetMapping("/getAllUnassigned")
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
	public List<LessonProgramResponse>getAllUnassigned(){
		return lessonProgramService.getAllLessonProgramUnassigned();
	}

	@GetMapping("/getAllAssigned")
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
	public List<LessonProgramResponse>getAllAssigned(){
		return lessonProgramService.getAllAssigned();
	}










}
