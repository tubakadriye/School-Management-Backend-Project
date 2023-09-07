package com.project.schoolmanagment.entity.abstracts;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.schoolmanagment.entity.concretes.user.UserRole;
import com.project.schoolmanagment.entity.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass //PROPERTIES will be used in child classes.
//since we do not have @Entity annotation this class will not be a table
@Data // @getter @setter @requiredargs etc.
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//is a kind of builder design pattern implementation
// parent class https://www.baeldung.com/lombok-builder-inheritance
public abstract class User { // abstract class: cannot create an object

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd") // it adds shape
    private LocalDate birthDay;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private Gender gender;



}
