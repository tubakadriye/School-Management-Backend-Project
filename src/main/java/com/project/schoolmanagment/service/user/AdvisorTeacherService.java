package com.project.schoolmanagment.service.user;


import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.mappers.user.AdvisorTeacherMapper;
import com.project.schoolmanagment.repository.user.AdvisorTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdvisorTeacherService {

    private final AdvisorTeacherRepository advisorTeacherRepository;
    private final UserRoleService userRoleService;
    private final AdvisorTeacherMapper advisorTeacherMapper;
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
}
