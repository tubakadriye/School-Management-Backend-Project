package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.entity.abstracts.User;
import com.project.schoolmanagment.entity.concretes.Student;
import com.project.schoolmanagment.entity.concretes.Teacher;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.request.StudentRequest;
import com.project.schoolmanagment.payload.request.TeacherRequest;
import com.project.schoolmanagment.payload.request.abstracts.BaseUserRequest;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.*;
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

	private final GuestUserRepository guestUserRepository;

	/**
	 *
	 * @param user a kind of entity that will be validated
	 * @param baseUserRequest DTO from UI to be changed
	 * @return true if they are the same
	 */
	public static boolean checkUniqueProperties(User user, BaseUserRequest baseUserRequest){
		return user.getSsn().equalsIgnoreCase(baseUserRequest.getSsn())
				|| user.getPhoneNumber().equalsIgnoreCase(baseUserRequest.getPhoneNumber())
				|| user.getUsername().equalsIgnoreCase(baseUserRequest.getUsername());
	}

	public static boolean checkUniquePropertiesForTeacher(Teacher teacher, TeacherRequest teacherRequest){
		return checkUniqueProperties(teacher,teacherRequest)
				|| teacher.getEmail().equalsIgnoreCase(teacherRequest.getEmail());
	}

	public static boolean checkUniquePropertiesForStudent(Student student, StudentRequest studentRequest){
		return checkUniqueProperties(student,studentRequest)
				|| student.getEmail().equalsIgnoreCase(studentRequest.getEmail());
	}


	public void checkDuplicate(String... values) {
		String username = values[0];
		String ssn = values[1];
		String phone = values[2];
		String email = "";

		if (values.length == 4) {
			email = values[3];
		}

		if (adminRepository.existsByUsername(username) || deanRepository.existsByUsername(username) ||
				studentRepository.existsByUsername(username) || teacherRepository.existsByUsername(username) ||
				viceDeanRepository.existsByUsername(username) || guestUserRepository.existsByUsername(username)) {
			throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
		} else if (adminRepository.existsBySsn(ssn) || deanRepository.existsBySsn(ssn) ||
				studentRepository.existsBySsn(ssn) || teacherRepository.existsBySsn(ssn) ||
				viceDeanRepository.existsBySsn(ssn) || guestUserRepository.existsBySsn(ssn)) {
			throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
		} else if (adminRepository.existsByPhoneNumber(phone) || deanRepository.existsByPhoneNumber(phone) ||
				studentRepository.existsByPhoneNumber(phone) || teacherRepository.existsByPhoneNumber(phone) ||
				viceDeanRepository.existsByPhoneNumber(phone) || guestUserRepository.existsByPhoneNumber(phone)) {
			throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone));
		} else if (studentRepository.existsByEmail(email) || teacherRepository.existsByEmail(email)) {
			throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, email));
		}
	}
}
