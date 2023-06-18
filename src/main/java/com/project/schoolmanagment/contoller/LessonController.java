package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.request.LessonRequest;
import com.project.schoolmanagment.payload.response.LessonResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("lessons")
@RequiredArgsConstructor
public class LessonController {


	private final LessonService lessonService;

	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
	@PostMapping("/save")
	public ResponseMessage<LessonResponse> saveLesson(@RequestBody @Valid LessonRequest lessonRequest){
			return lessonService.saveLesson(lessonRequest);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
	@DeleteMapping("/delete/{id}")
	public ResponseMessage deleteLesson(@PathVariable Long id){
		return lessonService.deleteLessonById(id);
	}



}
