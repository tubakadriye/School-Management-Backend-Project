package com.project.schoolmanagment.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Meet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd")
    private LocalDate date;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime startTime;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime stopTime;

    // advisory teacher has many meets
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("teacher") // not ignore all properties, just one value, if two : {"teacher","address"}
    private AdvisoryTeacher advisoryTeacher;


    @ManyToMany
    @JoinTable(
            name = "meet_student_table",
            joinColumns = @JoinColumn(name = "meet_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student>studentList;

}
