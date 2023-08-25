package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final AdminRepository adminRepository;
    private final DeanRepository deanRepository;
    private final ViceDeanRepository viceDeanRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    public void checkDuplicate(String... values){
        String username = values[0];
        String ssn = values[1];
        String phone = values[2];
        String email = "";

        if (values.length == 4){
            email = values[3];
        }

        if (adminRepository.existsByUsername(username) || deanRepository.existsByUsername(username) ||
                studentRepository.existsByUsername(username) || teacherRepository.existsByUsername(username) ||
                viceDeanRepository.existsByUsername(username)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME, username)); //string.format it will inject the username
        } else if (adminRepository.existsByUsername(ssn) || deanRepository.existsByUsername(ssn) ||
                studentRepository.existsByUsername(ssn) || teacherRepository.existsByUsername(ssn) ||
                viceDeanRepository.existsByUsername(ssn)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, ssn)); //string.format it will inject the username
        }else if (adminRepository.existsByUsername(email) || deanRepository.existsByUsername(email) ||
                studentRepository.existsByUsername(email) || teacherRepository.existsByUsername(email) ||
                viceDeanRepository.existsByUsername(email)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email)); //string.format it will inject the username
        } else if (adminRepository.existsByUsername(phone) || deanRepository.existsByUsername(phone) ||
                studentRepository.existsByUsername(phone) || teacherRepository.existsByUsername(phone) ||
                viceDeanRepository.existsByUsername(phone)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone)); //string.format it will inject the username
        }

    }


}
