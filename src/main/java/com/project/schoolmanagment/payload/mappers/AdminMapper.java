package com.project.schoolmanagment.payload.mappers;


import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.payload.request.user.AdminRequest;
import com.project.schoolmanagment.payload.response.user.AdminResponse;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    /*

    @param adminRequest DTO from UI
    @return entity
     */

    public Admin mapAdminRequestToAdmin(AdminRequest adminRequest){ //builder heps us map admin with adminrequest
        return Admin.builder()
                .username(adminRequest.getUsername())
                .name(adminRequest.getName())
                .surname(adminRequest.getSurname())
                .password(adminRequest.getPassword())
                .ssn(adminRequest.getSsn())
                .birthDay(adminRequest.getBirthDay())
                .birthPlace(adminRequest.getBirthPlace())
                .phoneNumber(adminRequest.getPhoneNumber())
                .gender(adminRequest.getGender())
                .build();  //to have this style we need to have builder or superbuilder.
        //if a class is inherited we use superbuilder.
    }

    public AdminResponse mapAdminToAdminResponse(Admin admin){
        return AdminResponse.builder()
                .userId(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .surname(admin.getSurname())
                .phoneNumber(admin.getPhoneNumber())
                .birthDay(admin.getBirthDay())
                .birthPlace(admin.getBirthPlace())
                .gender(admin.getGender())
                .ssn(admin.getSsn())
                .build();  //to have this style we need to have builder or superbuilder.
        //if a class is inherited we use superbuilder.
    }

}
