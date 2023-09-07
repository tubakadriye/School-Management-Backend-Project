package com.project.schoolmanagment.controller.business;

import com.project.schoolmanagment.entity.concretes.business.Lesson;
import com.project.schoolmanagment.payload.request.business.LessonRequest;
import com.project.schoolmanagment.payload.response.business.LessonResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.service.business.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonResponse>saveLesson(@Valid @RequestBody LessonRequest lessonRequest){
        return lessonService.saveLesson(lessonRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteLessonById(@PathVariable Long id){
        return lessonService.deleteLessonById(id);

    }

    @GetMapping("/getLessonByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonResponse>getLessonByLessonName(@RequestParam String lessonName){
        return lessonService.getLessonByLessonName(lessonName);
    }

    @GetMapping("/getAllLessonByLessonId")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Set<Lesson> getAllLessonsByLessonId(@RequestParam(name = "lessonId") Set<Long> idSet) {
        return lessonService.getAllLessonsByLessonId(idSet);
    }

    @GetMapping("/findLessonByPage")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Page<LessonResponse> findLessonByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type){
        return lessonService.findLessonByPage(page,size,sort,type);
    }




}
