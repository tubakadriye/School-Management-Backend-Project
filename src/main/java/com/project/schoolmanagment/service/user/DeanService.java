package com.project.schoolmanagment.service.user;


import com.project.schoolmanagment.entity.concretes.user.Dean;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.user.DeanMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.DeanRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.DeanResponse;
import com.project.schoolmanagment.repository.user.AdminRepository;
import com.project.schoolmanagment.repository.user.DeanRepository;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeanService {

    private final DeanRepository deanRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final AdminRepository adminRepository;
    private final UserRoleService userRoleService;
    private final DeanMapper deanMapper;
    private final PasswordEncoder passwordEncoder;

    //exception handling is done here
    private Dean isDeanExists(Long id){
        return deanRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));
    }

    public ResponseMessage<DeanResponse>saveDean(DeanRequest deanRequest){
        uniquePropertyValidator.checkDuplicate(deanRequest.getUsername(),
                deanRequest.getSsn(), deanRequest.getPhoneNumber());

        Dean dean = deanMapper.mapDeanRequestToDean(deanRequest);

        dean.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        dean.setPassword(passwordEncoder.encode(deanRequest.getPassword())); // we need to encode the password before saving
        Dean savedDean = deanRepository.save(dean);

        return ResponseMessage.<DeanResponse> builder()
                .message(SuccessMessages.DEAN_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(deanMapper.mapDeanToDeanResponse(savedDean))
                .build();
    }


    public ResponseMessage<DeanResponse> updateDeanById(Long id, DeanRequest deanRequest) {

        Dean dean = isDeanExists(id);
        uniquePropertyValidator.checkUniqueProperties(dean,deanRequest);
        Dean updatedDean = deanMapper.mapDeanRequestToUpdatedDean(deanRequest,id);
        Dean savedDean = deanRepository.save(updatedDean); // because
        return ResponseMessage.<DeanResponse>builder()
                .message(SuccessMessages.DEAN_UPDATE)
                .httpStatus(HttpStatus.OK) //return response
                .object(deanMapper.mapDeanToDeanResponse(savedDean)) //and map it again
                .build();

    }
}
