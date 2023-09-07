package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.ChooseLessonTeacherRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    //it should always be public
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse>saveTeacher(@RequestBody @Valid TeacherRequest teacherRequest){
        return teacherService.saveTeacher(teacherRequest);

    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse>updateTeacher(@RequestBody @Valid TeacherRequest teacherRequest,
                                                         @PathVariable Long userId){
        return teacherService.updateTeacher(teacherRequest, userId);
    }

    @GetMapping("/getTeacherByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<TeacherResponse> getTeacherByName(@RequestParam(name = "name")String teacherName){
        return teacherService.getTeacherByName(teacherName);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<TeacherResponse>getAllTeacher(){
        return teacherService.getAllTeacher();
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/chooseLesson")
    public ResponseMessage<TeacherResponse>chooseLesson(@RequestBody @Valid ChooseLessonTeacherRequest chooseLessonTeacherRequest) {
        return teacherService.chooseLesson(chooseLessonTeacherRequest);
    }

    // TUBA
    //TODO
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage deleteTeacherById(@PathVariable Long id){
        //return teacherService.deleteTeacherById(id);
        return null;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getSavedTeacherById/{id}")
    public ResponseMessage<TeacherResponse> findTeacherById(@PathVariable Long id){
        return teacherService.getTeacherById(id);
    }


    // NACI
    //TODO
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllTeacherByPage")
    public Page<TeacherResponse> getAllTeacherByPage(
            @RequestParam(value = "page")int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type){
        //return teacherService.getAllTeacherByPage(page,size,sort,type);
        return null;
    }

    // TODO
    // program to interface not in implementation
}
