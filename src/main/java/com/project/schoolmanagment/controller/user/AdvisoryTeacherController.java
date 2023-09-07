package com.project.schoolmanagment.controller.user;


import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.AdvisoryTeacherResponse;
import com.project.schoolmanagment.service.user.AdvisorTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/advisoryTeacher")
@RestController
@RequiredArgsConstructor
public class AdvisoryTeacherController {

    private final AdvisorTeacherService advisorTeacherService;

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAll")
    public List<AdvisoryTeacherResponse> getAllAdvisoryTeacher(){
        return advisorTeacherService.getAllAdvisoryTeacher();

    }


    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllAdvisoryTeacherByPage")
    public Page<AdvisoryTeacherResponse> getAllAdvisoryTeacherByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return advisorTeacherService.getAllAdvisorTeacherByPage(page,size,sort,type);

    }


    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage<AdvisoryTeacher>deleteAdvisoryTeacher(@PathVariable Long id){
        return advisorTeacherService.deleteAdvisoryTeacher(id);

    }
}
