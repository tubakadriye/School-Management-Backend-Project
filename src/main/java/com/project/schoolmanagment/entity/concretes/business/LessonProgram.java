package com.project.schoolmanagment.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.schoolmanagment.entity.concretes.user.Student;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.Day;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    // all education terms have lessonprogram.

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime stopTime;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "lesson_program_lesson",
            joinColumns = @JoinColumn(name = "lesson_program_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lessons;

    //educationTerm -> LessonProgram -> Lesson

    //an education term has many lesson programs
    @ManyToOne(cascade = CascadeType.PERSIST)
    private EducationTerm educationTerm;

    //TODO learn about FetchTypes
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(mappedBy = "lessonsProgramList", fetch = FetchType.EAGER)
    private Set<Teacher>teachers;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(mappedBy = "lessonsProgramList", fetch = FetchType.EAGER)
    private Set<Student>students;

    @PreRemove // implemented before deleting a lesson program
    private void removeLessonProgramFromStudentAndTeacher(){
        teachers.forEach(teacher -> teacher.getLessonsProgramList().remove(this));
        students.forEach(student -> student.getLessonsProgramList().remove(this));
    }


}
