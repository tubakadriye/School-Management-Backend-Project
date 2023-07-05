package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.Meet;
import com.project.schoolmanagment.entity.concretes.Student;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.mappers.MeetDto;
import com.project.schoolmanagment.payload.request.MeetRequest;
import com.project.schoolmanagment.payload.response.MeetResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.MeetRepository;
import com.project.schoolmanagment.repository.StudentRepository;
import com.project.schoolmanagment.utils.TimeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetService {


	private final MeetRepository meetRepository;
	private final StudentRepository studentRepository;
	private final AdvisoryTeacherService advisoryTeacherService;
	private final StudentService studentService;
	private final MeetDto meetDto;

	public ResponseMessage<MeetResponse> saveMeet(String username, MeetRequest meetRequest){
		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisorTeacherByUsername(username);
		TimeControl.checkTimeWithException(meetRequest.getStartTime(),meetRequest.getStopTime());

		for (Long studentId : meetRequest.getStudentIds()){
			studentService.isStudentsExist(studentId);
			checkMeetConflict(studentId,meetRequest.getDate(),meetRequest.getStartTime(),meetRequest.getStopTime());
		}
		List<Student>students = studentService.getStudentById(meetRequest.getStudentIds());
		Meet meet = new Meet();
		meet.setDate(meetRequest.getDate());
		meet.setStartTime(meetRequest.getStartTime());
		meet.setStopTime(meetRequest.getStopTime());
		meet.setStudentList(students);
		meet.setDescription(meetRequest.getDescription());
		meet.setAdvisoryTeacher(advisoryTeacher);
		Meet savedMeet = meetRepository.save(meet);

		return ResponseMessage.<MeetResponse>builder()
				.message("Meet successfully saved")
				.object(meetDto.mapMeetToMeetResponse(savedMeet))
				.httpStatus(HttpStatus.OK)
				.build();
	}

	private void checkMeetConflict(Long studentId, LocalDate date, LocalTime startTime, LocalTime stopTime){
		List<Meet>meets = meetRepository.findByStudentList_IdEquals(studentId);
		for (Meet meet :meets){
			LocalTime existingStartTime = meet.getStartTime();
			LocalTime existingStopTime = meet.getStopTime();

			if(meet.getDate().equals(date) &&
					((startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) ||
							(stopTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime)) ||
							(startTime.isBefore(existingStartTime) && stopTime.isAfter(existingStopTime)) ||
							(startTime.equals(existingStartTime) && stopTime.equals(existingStopTime)))){
				throw new ConflictException("meet hours has conflict with existing meets");
			}
		}
	}

	public List<MeetResponse>getAll(){
		return meetRepository.findAll()
				.stream()
				.map(meetDto::mapMeetToMeetResponse)
				.collect(Collectors.toList());
	}



}
