package com.project.schoolmanagment.service;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.mappers.AdvisoryTeacherDto;
import com.project.schoolmanagment.repository.AdvisoryTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvisoryTeacherService {


	private final AdvisoryTeacherRepository advisoryTeacherRepository;

	private final UserRoleService userRoleService;

	private final AdvisoryTeacherDto advisoryTeacherDto;

	public void saveAdvisoryTeacher(Teacher teacher){

		AdvisoryTeacher advisoryTeacher = advisoryTeacherDto.mapTeacherToAdvisoryTeacher(teacher);
		advisoryTeacher.setUserRole(userRoleService.getUserRole(RoleType.ADVISORY_TEACHER));

		advisoryTeacherRepository.save(advisoryTeacher);

	}


	public void updateAdvisoryTeacher(boolean status, Teacher teacher){
		//we are checking the DB to find the correct advisory teacher
		Optional<AdvisoryTeacher>advisoryTeacher = advisoryTeacherRepository.getAdvisoryTeacherByTeacher_Id(teacher.getId());

		AdvisoryTeacher.AdvisoryTeacherBuilder advisoryTeacherBuilder = AdvisoryTeacher.builder()
				.teacher(teacher)
				.userRole(userRoleService.getUserRole(RoleType.ADVISORY_TEACHER));

		//do we really have an advisory teacher in DB
		if(advisoryTeacher.isPresent()){
			//will be this new updated teacher really an advisory teacher
			if(status){
				advisoryTeacherBuilder.id(advisoryTeacher.get().getId());
				advisoryTeacherRepository.save(advisoryTeacherBuilder.build());
			} else {
				//these teacher is not advisory teacher anymore
				advisoryTeacherRepository.deleteById(advisoryTeacher.get().getId());
			}
		}
	}



}
