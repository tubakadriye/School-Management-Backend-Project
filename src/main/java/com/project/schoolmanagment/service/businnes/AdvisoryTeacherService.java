package com.project.schoolmanagment.service.businnes;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.AdvisoryTeacherMapper;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.response.AdvisorTeacherResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.businnes.AdvisoryTeacherRepository;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.user.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvisoryTeacherService {


	private final AdvisoryTeacherRepository advisoryTeacherRepository;

	private final UserRoleService userRoleService;

	private final AdvisoryTeacherMapper advisoryTeacherMapper;

	private final PageableHelper pageableHelper;

	public void saveAdvisoryTeacher(Teacher teacher){

		AdvisoryTeacher advisoryTeacher = advisoryTeacherMapper.mapTeacherToAdvisoryTeacher(teacher);
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
		} else {
			advisoryTeacherRepository.save(advisoryTeacherBuilder.build());
		}
	}

	public AdvisoryTeacher getAdvisoryTeacherById(Long advisoryTeacherId){
		return advisoryTeacherRepository
				.findById(advisoryTeacherId)
				.orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE,advisoryTeacherId)));
	}

	public List<AdvisorTeacherResponse> getAll(){
		return advisoryTeacherRepository.findAll()
				.stream()
				.map(advisoryTeacherMapper::mapAdvisorTeacherToAdvisorTeacherResponse)
				.collect(Collectors.toList());
	}

	public Page<AdvisorTeacherResponse> getAllAdvisorTeacherByPage(int page, int size, String sort, String type){
		Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
		return advisoryTeacherRepository
				.findAll(pageable)
				.map(advisoryTeacherMapper::mapAdvisorTeacherToAdvisorTeacherResponse);
	}

	public ResponseMessage<AdvisoryTeacher> deleteAdvisorTeacherById(Long id){
		AdvisoryTeacher advisoryTeacher = getAdvisoryTeacherById(id);
		advisoryTeacherRepository.deleteById(advisoryTeacher.getId());
		return ResponseMessage.<AdvisoryTeacher>builder()
				.message(SuccessMessages.ADVISOR_TEACHER_DELETE)
				.httpStatus(HttpStatus.OK)
				.build();
	}

	public AdvisoryTeacher getAdvisorTeacherByUsername(String username){
		return advisoryTeacherRepository
				.findByTeacher_UsernameEquals(username)
				.orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE_WITH_USERNAME,username)));
	}



}
