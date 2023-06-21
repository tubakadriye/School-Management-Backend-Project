package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.Teacher;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AdvisoryTeacherDto {

	public AdvisoryTeacher mapTeacherToAdvisoryTeacher(Teacher teacher){
		return AdvisoryTeacher.builder()
				.teacher(teacher)
				.build();
	}
}
