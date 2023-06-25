package com.project.schoolmanagment.contoller;

import com.project.schoolmanagment.payload.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

	public ResponseMessage<>saveStudent(@RequestBody @Valid )
}
