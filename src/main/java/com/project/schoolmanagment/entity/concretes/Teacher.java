package com.project.schoolmanagment.entity.concretes;


import com.project.schoolmanagment.entity.abstracts.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Teacher extends User {

	//TODO learn about cascade types and orphanRemoval
	@OneToOne(mappedBy = "teacher", cascade = CascadeType.PERSIST ,orphanRemoval = true)
	private AdvisoryTeacher advisoryTeacher;

	@Column(name = "isAdvisor")
	private Boolean isAdvisor;

	@Column(unique = true)
	private String email;


	@OneToMany(mappedBy = "teacher",cascade = CascadeType.REMOVE)
	private List<StudentInfo> studentInfos;












}
