package com.project.schoolmanagment.controller.business;

import com.project.schoolmanagment.payload.request.business.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.business.UpdateStudentInfoRequest;
import com.project.schoolmanagment.payload.response.business.StudentInfoResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.service.business.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {

    private final StudentInfoService studentInfoService;

    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save")
    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest,
                                                                @RequestBody @Valid StudentInfoRequest studentInfoRequest){

        return studentInfoService.saveStudentInfo(httpServletRequest, studentInfoRequest);

    }

    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @PutMapping("/update/{studentInfoId}")
    public ResponseMessage<StudentInfoResponse>updateStudentInfo(@RequestBody @Valid UpdateStudentInfoRequest updateStudentInfoRequest,
                                                                 @PathVariable Long studentInfoId){
        return studentInfoService.updateStudentInfo(updateStudentInfoRequest, studentInfoId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @DeleteMapping("/delete/{studentInfoId}")
    public ResponseMessage delete (@PathVariable Long studentInfoId){
        return studentInfoService.deleteStudentInfo(studentInfoId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllStudentInfoByPage")
    public Page<StudentInfoResponse> getAllStudentInfoByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return studentInfoService.getAllStudentInfoByPage(page, size, sort, type);
    }

    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllForStudent")
    public ResponseEntity<Page<StudentInfoResponse>>getAllForStudent(
            HttpServletRequest httpServletRequest,
            @RequestParam(value= "page") int page,
            @RequestParam(value = "size") int size
    ){
        return new ResponseEntity<>(studentInfoService.getAllForStudent(httpServletRequest, page, size), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllForTeacher")
    public ResponseEntity<Page<StudentInfoResponse>>getAllForTeacher(
            HttpServletRequest httpServletRequest,
            @RequestParam(value= "page") int page,
            @RequestParam(value = "size") int size
    ){
        return new ResponseEntity<>(studentInfoService.getAllForTeacher(httpServletRequest,page, size), HttpStatus.OK);
    }


}
