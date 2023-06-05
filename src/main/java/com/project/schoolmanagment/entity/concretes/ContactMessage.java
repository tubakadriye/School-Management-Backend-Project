package com.project.schoolmanagment.entity.concretes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
public class ContactMessage {

	//TODO check all generation types and strategies
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	private String subject;

	private String message;

	private LocalDate date;

}
