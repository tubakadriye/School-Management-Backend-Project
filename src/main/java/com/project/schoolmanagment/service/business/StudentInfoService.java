package com.project.schoolmanagment.service.business;

import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.entity.concretes.business.Lesson;
import com.project.schoolmanagment.entity.concretes.business.StudentInfo;
import com.project.schoolmanagment.entity.concretes.user.Student;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.Note;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.business.StudentInfoMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.business.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.business.UpdateStudentInfoRequest;
import com.project.schoolmanagment.payload.response.business.StudentInfoResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.repository.business.StudentInfoRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.user.StudentService;
import com.project.schoolmanagment.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
@RequiredArgsConstructor
public class StudentInfoService {

    private final StudentInfoRepository studentInfoRepository;
    private final EducationTermService educationTermService;
    private final LessonService lessonService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StudentInfoMapper studentInfoMapper;
    private final PageableHelper pageableHelper;

    @Value("${midterm.exam.impact.percentage}")
    private Double midtermExamPercentage;

    @Value("${final.exam.impact.percentage}")
    private Double finalExamPercentage;


    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest, StudentInfoRequest studentInfoRequest) {
    String teacherUsername = (String) httpServletRequest.getAttribute("username");
    Teacher teacher = teacherService.getTeacherByUsername(teacherUsername);
    Student student =  studentService.isStudentExist(studentInfoRequest.getStudentId());
    EducationTerm educationTerm = educationTermService.isEducationTermExist(studentInfoRequest.getEducationTermId());
    Lesson lesson = lessonService.isLessonExistById(studentInfoRequest.getLessonId());
    checkSameLesson(studentInfoRequest.getStudentId(), lesson.getLessonName());

    double examAverage = calculateExamAverage(studentInfoRequest.getMidtermExam(), studentInfoRequest.getFinalExam());
    //we need grade calculation
    Note note = checkLetterGrade(examAverage);
    //we need mappers
    StudentInfo studentInfo = studentInfoMapper.mapStudentInfoRequestToStudentInfo(
            studentInfoRequest,
            note,
            examAverage);
    //we need to set values that do not exist
        studentInfo.setStudent(student);
        studentInfo.setEducationTerm(educationTerm);
        studentInfo.setTeacher(teacher);
        studentInfo.setLesson(lesson);
        StudentInfo savedStudentInfo = studentInfoRepository.save(studentInfo);
    return ResponseMessage.<StudentInfoResponse>builder()
            .message(SuccessMessages.STUDENT_INFO_SAVE)
            .object(studentInfoMapper.mapStudentInfoToStudentInfoResponse(savedStudentInfo))
            .httpStatus(HttpStatus.OK)
            .build();


    }

    private void checkSameLesson(Long studentId, String lessonName){
        boolean isLessonDuplicationExist = studentInfoRepository.getAllByStudentId_Id(studentId)
                .stream()
                .anyMatch((e)->e.getLesson().getLessonName().equalsIgnoreCase(lessonName));
        if(isLessonDuplicationExist){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_LESSON_MESSAGE, lessonName));


        }
    }

    private Double calculateExamAverage(Double midtermExam, Double finalExam){
        return ((midtermExam*midtermExamPercentage) + (finalExam*finalExamPercentage));
    }

    private Note checkLetterGrade(Double average) {
        if(average<50.0) {
            return Note.FF;
        } else if (average>=50.0 && average<55) {
            return Note.DD;
        } else if (average>=55.0 && average<60) {
            return Note.DC;
        } else if (average>=60.0 && average<65) {
            return Note.CC;
        } else if (average>=65.0 && average<70) {
            return Note.CB;
        } else if (average>=70.0 && average<75) {
            return Note.BB;
        } else if (average>=75.0 && average<80) {
            return Note.BA;
        } else {
            return Note.AA;
        }

    }

    public StudentInfo isStudentInfoExistById(Long id){
        return studentInfoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.STUDENT_INFO_NOT_FOUND,id)));
    }

    public ResponseMessage<StudentInfoResponse> updateStudentInfo(UpdateStudentInfoRequest updateStudentInfoRequest, Long studentInfoId) {
        Lesson lesson = lessonService.isLessonExistById(updateStudentInfoRequest.getLessonId());
        StudentInfo studentInfo = isStudentInfoExistById(studentInfoId);
        EducationTerm educationTerm = educationTermService.isEducationTermExist(updateStudentInfoRequest.getEducationTermId());
        Double averageNote = calculateExamAverage(updateStudentInfoRequest.getMidtermExam(), updateStudentInfoRequest.getFinalExam());
        Note note = checkLetterGrade(averageNote);
        StudentInfo updatedStudentInfo = studentInfoMapper.mapStudentInfoUpdateToStudentInfo(updateStudentInfoRequest, studentInfoId,lesson, educationTerm, note,averageNote);
        updatedStudentInfo.setStudent(studentInfo.getStudent());
        updatedStudentInfo.setTeacher(studentInfo.getTeacher());
        StudentInfo savedStudentInfo = studentInfoRepository.save(updatedStudentInfo);

        return ResponseMessage.<StudentInfoResponse>builder()
                .message(SuccessMessages.STUDENT_INFO_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(studentInfoMapper.mapStudentInfoToStudentInfoResponse(savedStudentInfo))
                .build();
    }

    public Page<StudentInfoResponse> getAllForStudent(HttpServletRequest httpServletRequest, int page, int size) {
        String username = (String) httpServletRequest.getAttribute("username");
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size);
        return studentInfoRepository.findByTeacherId_UsernameEquals(username, pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);

    }

    public ResponseMessage deleteStudentInfo(Long studentInfoId) {
        StudentInfo studentInfo = isStudentInfoExistById((studentInfoId));
        studentInfoRepository.deleteById(studentInfo.getId());

        return ResponseMessage.builder()
                .message(SuccessMessages.STUDENT_INFO_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Page<StudentInfoResponse> getAllStudentInfoByPage(int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page,size,sort,type);
        return studentInfoRepository
                .findAll(pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);
    }

    public Page<StudentInfoResponse> getAllForTeacher(HttpServletRequest httpServletRequest, int page, int size) {
        String username= (String) httpServletRequest.getAttribute("username");
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size);
        return studentInfoRepository.findByTeacherId_UsernameEquals(username, pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);


    }
}
