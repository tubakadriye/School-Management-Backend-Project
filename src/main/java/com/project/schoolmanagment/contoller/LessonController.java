package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lessons")
@RequiredArgsConstructor
public class LessonController {


	private final LessonService lessonService;

	public ResponseMessage<> saveLesson(){

	}



}
