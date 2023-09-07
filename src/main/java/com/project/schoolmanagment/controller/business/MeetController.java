package com.project.schoolmanagment.controller.business;


import com.project.schoolmanagment.payload.request.business.MeetRequest;
import com.project.schoolmanagment.payload.response.business.MeetResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.service.business.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetController {
    private final MeetService meetService;

    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save")
    public ResponseMessage<MeetResponse>saveMeet(HttpServletRequest httpServletRequest,
                                                 @RequestBody @Valid MeetRequest meetRequest){
        return meetService.saveMeet(meetRequest,httpServletRequest);

    }

    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @PutMapping("/update/{meetId}")
    public ResponseMessage<MeetResponse>updateMeet(@RequestBody @Valid MeetRequest meetRequest,
                                                   @PathVariable Long meetId){
        return meetService.updateMeet(meetRequest,meetId);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllMeetByAdvisorTeacherAsList")
    public ResponseEntity<List<MeetResponse>>getAllMeetByTeacher(HttpServletRequest httpServletRequest){ //response entity is related with api, cennot be in service.
        return ResponseEntity.ok(meetService.getAllMeetByAdvisorTeacherAslist(httpServletRequest));

    }

    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllMeetByStudent")
    public ResponseEntity<List<MeetResponse>>getAllMeetByStudent(HttpServletRequest httpServletRequest){ //response entity is related with api, cennot be in service.
        return ResponseEntity.ok(meetService.getAllMeetByStudent(httpServletRequest));

    }
}
