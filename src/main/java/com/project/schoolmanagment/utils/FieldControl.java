package com.project.schoolmanagment.utils;

import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FieldControl {

	private final AdminRepository adminRepository;

	private final DeanRepository deanRepository;

	private final ViceDeanRepository viceDeanRepository;

	private final StudentRepository studentRepository;

	private final TeacherRepository teacherRepository;

	private final GuestUserRepository guestUserRepository;


	//As a requirement all Admin,ViceAdmin,Dean, Student, Teacher, guestUser
	//should have unique userName, email, ssn and phone number
	public void checkDuplicate(String username, String ssn,String phone){

		if(adminRepository.existsByUsername(username) ||
				deanRepository.existsByUsername(username) ||
				studentRepository.existsByUsername(username) ||
				teacherRepository.existsByUsername(username) ||
				viceDeanRepository.existsByUsername(username) ||
				guestUserRepository.existsByUsername(username)){
			throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_USERNAME,username));
		} else if (adminRepository.existsBySsn(ssn) ||
				deanRepository.existsBySsn(ssn) ||
				studentRepository.existsBySsn(ssn) ||
				teacherRepository.existsBySsn(ssn) ||
				viceDeanRepository.existsBySsn(ssn) ||
				guestUserRepository.existsBySsn(ssn) ){
			throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_SSN,ssn));
		} else if (adminRepository.existsByPhoneNumber(phone) ||
				deanRepository.existsByPhoneNumber(phone) ||
				studentRepository.existsByPhoneNumber(phone) ||
				teacherRepository.existsByPhoneNumber(phone) ||
				viceDeanRepository.existsByPhoneNumber(phone) ||
				guestUserRepository.existsByPhoneNumber(phone)) {
			throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER,phone));
		}
	}

}
