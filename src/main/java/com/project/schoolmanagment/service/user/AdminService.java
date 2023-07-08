package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.Admin;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.AdminMapper;
import com.project.schoolmanagment.payload.request.AdminRequest;
import com.project.schoolmanagment.payload.response.AdminResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.repository.*;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;

	private final UserRoleService userRoleService;

	private final UniquePropertyValidator uniquePropertyValidator;

	private final PageableHelper pageableHelper;

	private final AdminMapper adminMapper;

	public ResponseMessage<AdminResponse> saveAdmin(AdminRequest adminRequest){

		uniquePropertyValidator.checkDuplicate(adminRequest.getUsername(), adminRequest.getSsn(), adminRequest.getPhoneNumber());

		Admin admin = adminMapper.mapAdminRequestToAdmin(adminRequest);
		admin.setBuilt_in(false);

		//if username is also Admin we are setting built_in prop. to FALSE
		if(Objects.equals(adminRequest.getUsername(),"Admin")){
			admin.setBuilt_in(true);
		}

		admin.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));

		//we will implement password encoder here

		Admin savedAdmin = adminRepository.save(admin);

		//In response message savedAdmin instance may not be sent back to front-end.
		return ResponseMessage.<AdminResponse>builder()
				.message(SuccessMessages.ADMIN_CREATE)
				.object(adminMapper.mapAdminToAdminResponse(savedAdmin))
				.build();
	}

	public Page<Admin> getAllAdminsByPage(int page, int size, String sort, String type){

		Pageable pageable = pageableHelper.getPageableWithProperties(page,size,sort,type);

		return adminRepository.findAll(pageable);
	}

	public String deleteAdminById(Long id){
		//we should check the database if it really exists
		Optional<Admin>admin = adminRepository.findById(id);
		if(!admin.isPresent()){
			throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE);
		} else if (admin.get().isBuilt_in()) {
			throw new ConflictException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
		}


		if (admin.isPresent()){
			adminRepository.deleteById(id);
			return SuccessMessages.ADMIN_DELETE;
		}
		return String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id);

	}


	public long countAllAdmins(){
		return adminRepository.count();
	}

}
