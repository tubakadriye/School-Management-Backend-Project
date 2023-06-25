package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.Student;
import com.project.schoolmanagment.payload.request.StudentRequest;

public class StudentDto {

	public Student mapStudentRequestToStudent(StudentRequest studentRequest){
		return Student.builder()
				.fatherName(studentRequest.getFatherName())
				.motherName(studentRequest.getMotherName())
				.birthDay(studentRequest.getBirthDay())
				.birthPlace(studentRequest.getBirthPlace())
				.name(studentRequest.getName())
				.surname(studentRequest.getSurname())
				.password(studentRequest.getPassword())
				.username(studentRequest.getUsername())
				.ssn(studentRequest.getSsn())
				.email(studentRequest.getEmail())
				.phoneNumber(studentRequest.getPhoneNumber())
				.gender(studentRequest.getGender())
				.build();
	}
}
