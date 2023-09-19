package com.project.schoolmanagment.service.business;

import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.entity.concretes.business.Lesson;
import com.project.schoolmanagment.entity.concretes.business.LessonProgram;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.business.LessonProgramMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.business.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.business.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.repository.business.LessonProgramRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final PageableHelper pageableHelper;
    private final DateTimeValidator dateTimeValidator;
    private final LessonProgramMapper lessonProgramMapper;

    //TODO
    //Program to interface not in implementation
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(LessonProgramRequest lessonProgramRequest) {

        //we need list of lessons to create a lesson program
        Set<Lesson> lessons = lessonService.getAllLessonsByLessonId(lessonProgramRequest.getLessonIdList());
        // we need an education term
        EducationTerm educationTerm = educationTermService.isEducationTermExist(lessonProgramRequest.getEducationTermId());
        //timevalidation
        dateTimeValidator.checkTimeWithException(lessonProgramRequest.getStartTime(),lessonProgramRequest.getStopTime());

        LessonProgram lessonProgram = lessonProgramMapper.mapLessonProgramRequestToLessonProgram(lessonProgramRequest, lessons, educationTerm);
        LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);


        return ResponseMessage.<LessonProgramResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(lessonProgramMapper.mapLessonProgramToLessonProgramResponse(savedLessonProgram))
                .build();
    }



    public Set<LessonProgramResponse> getLessonProgramByTeacher(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return lessonProgramRepository.getLessonProgramByTeachersUsername(username)
                .stream().map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());
    }


    public Set<LessonProgramResponse> getLessonProgramByStudent(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return lessonProgramRepository.getLessonProgramByStudentsUsername(username)
                .stream().map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());
    }

    public List<LessonProgramResponse> getAllLessonProgramAssigned() {

        return lessonProgramRepository.findByTeachers_IdNotNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public List<LessonProgramResponse> getAllLessonProgramUnassigned() {

        return lessonProgramRepository.findByTeachers_IdNotNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public Set<LessonProgram> getLessonProgramById(Set<Long>lessonProgramSet) {
        return lessonProgramSet.stream()
                .map(this::findLessonProgramById)
                .collect(Collectors.toSet());

    }

    private LessonProgram findLessonProgramById(Long id) {
        return lessonProgramRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE,id)));

    }

    public ResponseMessage deleteLessonProgramById(Long id) {

        findLessonProgramById(id);
        lessonProgramRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.LESSON_PROGRAM_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Page<LessonProgramResponse> getAllLessonProgramByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page,size,sort,type);
        return lessonProgramRepository
                .findAll(pageable)
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse);

    }

    public List<LessonProgramResponse> getAllLessonProgramList() {
        return lessonProgramRepository.findAll()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }
}
