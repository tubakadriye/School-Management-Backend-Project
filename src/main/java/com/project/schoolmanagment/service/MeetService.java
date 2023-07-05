package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.Meet;
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
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

		for (Long studentId : meetRequest.getStudentIds()){
			studentService.isStudentsExist(studentId);
		}

		return null;
	}

	private void checkMeetConflict(Long studentId, LocalDate date, LocalTime startTime, LocalTime stopTime){
		List<Meet>meets = meetRepository.findByStudentList_IdEquals(studentId);
		for (Meet meet :meets){
			LocalTime existingStartTime = meet.getStartTime();
			LocalTime existingStopTime = meet.getStopTime();
		}
	}



}
