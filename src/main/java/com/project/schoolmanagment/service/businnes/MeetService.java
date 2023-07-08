package com.project.schoolmanagment.service.businnes;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.Meet;
import com.project.schoolmanagment.entity.concretes.Student;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.MeetMapper;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.MeetRequest;
import com.project.schoolmanagment.payload.request.UpdateMeetRequest;
import com.project.schoolmanagment.payload.response.MeetResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.MeetRepository;
import com.project.schoolmanagment.repository.StudentRepository;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.user.StudentService;
import com.project.schoolmanagment.service.validator.TimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetService {

	private final MeetRepository meetRepository;
	private final AdvisoryTeacherService advisoryTeacherService;
	private final StudentService studentService;
	private final MeetMapper meetMapper;
	private final PageableHelper pageableHelper;

	public ResponseMessage<MeetResponse> saveMeet(HttpServletRequest httpServletRequest, MeetRequest meetRequest){
		String username = (String) httpServletRequest.getAttribute("username");
		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisorTeacherByUsername(username);
		TimeValidator.checkTimeWithException(meetRequest.getStartTime(),meetRequest.getStopTime());

		for (Long studentId : meetRequest.getStudentIds()){
			studentService.isStudentsExist(studentId);
			checkMeetConflict(studentId,meetRequest.getDate(),meetRequest.getStartTime(),meetRequest.getStopTime());
		}
		List<Student>students = studentService.getStudentById(meetRequest.getStudentIds());
		Meet meet = meetMapper.mapMeetRequestToMeet(meetRequest);
		meet.setStudentList(students);
		meet.setAdvisoryTeacher(advisoryTeacher);
		Meet savedMeet = meetRepository.save(meet);

		return ResponseMessage.<MeetResponse>builder()
				.message(SuccessMessages.MEET_SAVE)
				.object(meetMapper.mapMeetToMeetResponse(savedMeet))
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

	public ResponseMessage<MeetResponse>updateMeet(UpdateMeetRequest updateMeetRequest,Long meetId){
		Meet meet = isMeetExistById(meetId);
		TimeValidator.checkTimeWithException(updateMeetRequest.getStartTime(),updateMeetRequest.getStopTime());
		if(!(meet.getDate().equals(updateMeetRequest.getDate()) &&
				meet.getStartTime().equals(updateMeetRequest.getStartTime()) &&
				meet.getStopTime().equals(updateMeetRequest.getStopTime()))){
			for (Long studentId : updateMeetRequest.getStudentIds()){
				checkMeetConflict(studentId,updateMeetRequest.getDate()
						,updateMeetRequest.getStartTime()
						,updateMeetRequest.getStopTime());
			}
		}

		List<Student>students = studentService.getStudentById(updateMeetRequest.getStudentIds());
		Meet updateMeet = meetMapper.mapMeetUpdateRequestToMeet(updateMeetRequest,meetId);
		updateMeet.setStudentList(students);
		//TODO write new API for update the teacher
		updateMeet.setAdvisoryTeacher(meet.getAdvisoryTeacher());
		Meet updatedMeet = meetRepository.save(updateMeet);
		return ResponseMessage.<MeetResponse>builder()
				.message(SuccessMessages.MEET_UPDATE)
				.httpStatus(HttpStatus.OK)
				.object(meetMapper.mapMeetToMeetResponse(updatedMeet))
				.build();
	}

	public ResponseEntity getAllMeetByTeacher(HttpServletRequest httpServletRequest){
		String userName = (String) httpServletRequest.getAttribute("username");
		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisorTeacherByUsername(userName);
		List<MeetResponse> meetResponseList = meetRepository.getByAdvisoryTeacher_IdEquals(advisoryTeacher.getId())
				.stream()
				.map(meetMapper::mapMeetToMeetResponse).collect(Collectors.toList());
		return ResponseEntity.ok(meetResponseList);
	}

	public List<MeetResponse> getAllMeetByStudent (HttpServletRequest httpServletRequest){
		String userName = (String) httpServletRequest.getAttribute("username");
		Student student = studentService.isStudentsExistByUsername(userName);
		return meetRepository.findByStudentList_IdEquals(student.getId())
				.stream()
				.map(meetMapper::mapMeetToMeetResponse)
				.collect(Collectors.toList());
	}

	public Page<MeetResponse> getAllMeetByPage(int page, int size){
		Pageable pageable = pageableHelper.getPageableWithProperties(page, size);
		return meetRepository.findAll(pageable).map(meetMapper::mapMeetToMeetResponse);
	}

	public List<MeetResponse>getAll(){
		return meetRepository.findAll()
				.stream()
				.map(meetMapper::mapMeetToMeetResponse)
				.collect(Collectors.toList());
	}

	public ResponseMessage<MeetResponse>getMeetById(Long meetId){
		return ResponseMessage.<MeetResponse>builder()
				.message(SuccessMessages.MEET_FOUND)
				.httpStatus(HttpStatus.OK)
				.object(meetMapper.mapMeetToMeetResponse(isMeetExistById(meetId)))
				.build();
	}

	private Meet isMeetExistById(Long meetId){
		return meetRepository
				.findById(meetId).orElseThrow(
				()->new ResourceNotFoundException(String.format(ErrorMessages.MEET_NOT_FOUND_MESSAGE,meetId)));
	}

	public ResponseMessage delete(Long meetId){
		isMeetExistById(meetId);
		meetRepository.deleteById(meetId);
		return ResponseMessage.builder()
				.message(SuccessMessages.MEET_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public ResponseEntity<Page<MeetResponse>>getAllMeetByTeacher(HttpServletRequest httpServletRequest,
	                                                             int page,
	                                                             int size){
		String userName = (String) httpServletRequest.getAttribute("username");
		AdvisoryTeacher advisoryTeacher = advisoryTeacherService.getAdvisorTeacherByUsername(userName);
		Pageable pageable = pageableHelper.getPageableWithProperties(page,size);
		return ResponseEntity.ok(meetRepository.findByAdvisoryTeacher_IdEquals(advisoryTeacher.getId(), pageable)
				.map(meetMapper::mapMeetToMeetResponse));
	}


}
