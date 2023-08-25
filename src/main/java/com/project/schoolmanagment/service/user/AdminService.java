package com.project.schoolmanagment.service.user;


import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.AdminMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.AdminRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.AdminResponse;
import com.project.schoolmanagment.repository.user.AdminRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.ResourceClosedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final AdminRepository adminRepository;
    private final UserRoleService userRoleService;
    private UniquePropertyValidator uniquePropertyValidator;

    private final PageableHelper pageableHelper;

    public ResponseMessage<AdminResponse>saveAdmin(AdminRequest adminRequest){

        uniquePropertyValidator.checkDuplicate(adminRequest.getUsername(),
                                               adminRequest.getSsn(),
                                                adminRequest.getPhoneNumber());

        // we have an admit request we need to map it with admin.

        //we will make all the mappers inside the payload
        //we are map DTO-> Entity
        Admin admin = adminMapper.mapAdminRequestToAdmin(adminRequest); // weare mapping dto to entity

        admin.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));

        if(Objects.equals(adminRequest.getUsername(),"superAdmin")) {

        }

        Admin savedAdmin = adminRepository.save(admin); // and we are saving the entity


        // we are returning response DTO by mapping the saved version of admin
        return ResponseMessage.<AdminResponse>builder()
                .message(SuccessMessages.ADMIN_CREATE)
                .object(adminMapper.mapAdminToAdminResponse(savedAdmin))
                .build();
    }

    public long countAllAdmins(){
        return adminRepository.count();
    }

    public Page<Admin> getAllAdminsByPage(int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort,type );
        return adminRepository.findAll(pageable);

    }


    public String deleteById(Long id) {
        //we should check the database if this id really exists
        Optional<Admin>admin = adminRepository.findById(id);
        if(admin.isEmpty()){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, id));
        } else if (admin.get().isBuiltIn()){
            throw new ConflictException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
        adminRepository.deleteById(id);
        return SuccessMessages.ADMIN_DELETE;
    }
}
