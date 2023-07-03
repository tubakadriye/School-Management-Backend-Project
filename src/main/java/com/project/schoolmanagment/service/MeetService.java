package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.request.MeetRequest;
import com.project.schoolmanagment.payload.response.MeetResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.MeetRepository;
import com.project.schoolmanagment.repository.StudentRepository;
import com.project.schoolmanagment.utils.Messages;
import com.project.schoolmanagment.utils.TimeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetService {


	private final MeetRepository meetRepository;
	private final StudentRepository studentRepository;
	private final AdvisoryTeacherService advisoryTeacherService;
	private final StudentService studentService;

	public ResponseMessage<MeetResponse> saveMeet(String username, MeetRequest meetRequest){
		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisorTeacherByUsername(username);
		TimeControl.checkTimeWithException(meetRequest.getStartTime(),meetRequest.getStopTime());

		//TODO check all students exist and do we have any time conflict between meets of them

	}



}
