package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.business.ChooseLessonProgramWithIdRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    //Program to interface, not an implementation!


    private final StudentService studentService;


    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> saveStudent(@Valid @RequestBody StudentRequest studentRequest){
        return studentService.saveStudent(studentRequest);


    }

    @GetMapping("/changeStatus")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage changeStatus (@RequestParam Long id, @RequestParam boolean status){
        return studentService.changeStatus(id, status);
    }


    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> updateStudent(@PathVariable Long id,
                                                          @RequestBody @Valid StudentRequest studentRequest){
        return studentService.updateStudent(id, studentRequest);
    }


    @GetMapping("/getStudentByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<StudentResponse> getStudentByName(@RequestParam(name = "name") String studentName){
        return studentService.getStudentByName(studentName);

    }


    //url wrong in template
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllStudentByAdvisoryTeacherUsername")
    public List<StudentResponse>getAllByAdvisoryTeacherUsername(HttpServletRequest httpServletRequest){
        return studentService.getAllByAdvisoryTeacherUsername(httpServletRequest);
    }

    @PostMapping("/chooseLesson")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseMessage<StudentResponse>chooseLesson(HttpServletRequest httpServletRequest, @RequestBody @Valid ChooseLessonProgramWithIdRequest chooseLessonProgramWithIdRequest) {
        return studentService.chooseLesson(httpServletRequest, chooseLessonProgramWithIdRequest);
    } //we check who logged in


    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/getAllStudentByPage")
    public Page<StudentResponse> getAllStudentByPAge(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return studentService.getAllStudentByPAge(page,size,sort,type);

    }
}
