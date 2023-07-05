package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.Meet;
import com.project.schoolmanagment.payload.request.MeetRequest;
import com.project.schoolmanagment.payload.response.MeetResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MeetDto {

	public Meet mapMeetRequestToMeet(MeetRequest meetRequest){
		return null;
	}


	public MeetResponse mapMeetToMeetResponse(Meet meet){
		return MeetResponse.builder()
				.id(meet.getId())
				.date(meet.getDate())
				.startTime(meet.getStartTime())
				.stopTime(meet.getStopTime())
				.description((meet.getDescription()))
				.advisorTeacherId(meet.getAdvisoryTeacher().getId())
				.teacherSsn(meet.getAdvisoryTeacher().getTeacher().getSsn())
				.teacherName(meet.getAdvisoryTeacher().getTeacher().getName())
				.students(meet.getStudentList())
				.build();
	}
}
