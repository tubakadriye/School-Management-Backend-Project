package com.project.schoolmanagment.payload.mappers.business;

import com.project.schoolmanagment.entity.concretes.business.Meet;
import com.project.schoolmanagment.payload.request.business.MeetRequest;
import com.project.schoolmanagment.payload.response.business.MeetResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MeetMappers {

    public Meet mapMeetRequestToMeet(MeetRequest meetRequest){
        return Meet.builder()
                .date(meetRequest.getDate())
                .startTime(meetRequest.getStartTime())
                .stopTime(meetRequest.getStopTime())
                .description(meetRequest.getDescription())
                .build();
    }

    public MeetResponse mapMeetToMeetResponse(Meet meet){
        return MeetResponse.builder()
                .id(meet.getId())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .stopTime(meet.getStopTime())
                .description(meet.getDescription())
                .advisorTeacherId(meet.getAdvisoryTeacher().getId())
                .teacherSsn(meet.getAdvisoryTeacher().getTeacher().getSsn())
                .teacherName(meet.getAdvisoryTeacher().getTeacher().getName())
                .students(meet.getStudentList())
                .build();
    }

    public Meet mapMeetUpdateRequestToMeet(MeetRequest meetRequest, Long meetId){
        Meet meet = mapMeetRequestToMeet(meetRequest);
        meet.setId(meetId);
        return meet;

    }
}
