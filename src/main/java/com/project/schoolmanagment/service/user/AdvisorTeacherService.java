package com.project.schoolmanagment.service.user;


import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.user.AdvisorTeacherMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.AdvisoryTeacherResponse;
import com.project.schoolmanagment.repository.user.AdvisorTeacherRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdvisorTeacherService {

    private final AdvisorTeacherRepository advisorTeacherRepository;
    private final UserRoleService userRoleService;
    private final AdvisorTeacherMapper advisorTeacherMapper;
    private final PageableHelper pageableHelper;
    public void saveAdvisoryTeacher(Teacher teacher) {
        AdvisoryTeacher advisoryTeacher = advisorTeacherMapper.mapTeacherToAdvisorTeacher(teacher);
        advisoryTeacher.setUserRole(userRoleService.getUserRole(RoleType.ADVISORY_TEACHER));
        advisorTeacherRepository.save(advisoryTeacher);
    }

    public void updateAdvisorTeacher(boolean status,Teacher teacher){
        //status -> he/she WILL be advisory or NOT
        Optional<AdvisoryTeacher>advisoryTeacher = advisorTeacherRepository.getAdvisoryTeacherByTeacher_Id(teacher.getId());

        AdvisoryTeacher.AdvisoryTeacherBuilder advisoryTeacherBuilder = AdvisoryTeacher.builder()
                .teacher(teacher)
                .userRole(userRoleService.getUserRole(RoleType.ADVISORY_TEACHER));

        // do we really have an advisory teacher in DB with this id
        if(advisoryTeacher.isPresent()){
            //will be this new updated teacher really an advisory teacher
            if(status){
                advisoryTeacherBuilder.id(advisoryTeacher.get().getId());
                advisorTeacherRepository.save(advisoryTeacherBuilder.build());
            } else {
                //these teacher is not advisory teacher anymore
                advisorTeacherRepository.deleteById(advisoryTeacher.get().getId());
            }
        } else {
            //it does not exist in DB, but it will be created then.
            advisorTeacherRepository.save(advisoryTeacherBuilder.build());
        }
    }


    public AdvisoryTeacher getAdvisorTeacherById(Long id){
        return advisorTeacherRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));
    }

    public AdvisoryTeacher getAdvisorTeacherByUsername(String username){
        return advisorTeacherRepository
                .findByTeacher_UsernameEquals(username)
                .orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE_USERNAME,username)));
    }

    public List<AdvisoryTeacherResponse> getAllAdvisoryTeacher(){
        return advisorTeacherRepository.findAll()
                .stream()
                .map(advisorTeacherMapper::mapAdvisorTeacherToAdvisoryTeacherResponse)
                .collect(Collectors.toList());
    }


    public Page<AdvisoryTeacherResponse> getAllAdvisorTeacherByPage(int page, int size, String sort, String type) {

        return advisorTeacherRepository
                .findAll(pageableHelper.getPageableWithProperties(page,size,sort,type))
                .map(advisorTeacherMapper::mapAdvisorTeacherToAdvisoryTeacherResponse);

    }

    public ResponseMessage<AdvisoryTeacher> deleteAdvisoryTeacher(Long id) {
        AdvisoryTeacher advisoryTeacher = getAdvisorTeacherById(id);
        advisorTeacherRepository.deleteById(id);
        return ResponseMessage.<AdvisoryTeacher>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_DELETE)
                .object(advisoryTeacher)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
