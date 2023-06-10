package com.project.schoolmanagment.service;

import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.request.AdminRequest;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.*;
import com.project.schoolmanagment.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ConcurrentModificationException;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;

	private final DeanRepository deanRepository;

	private final ViceDeanRepository viceDeanRepository;

	private final StudentRepository studentRepository;

	private final TeacherRepository teacherRepository;

	private final GuestUserRepository guestUserRepository;


	public ResponseMessage save (AdminRequest adminRequest){

	}
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
		} else if (

		) {

		}
	}

}
