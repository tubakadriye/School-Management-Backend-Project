package com.project.schoolmanagment.entity.concretes.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.schoolmanagment.entity.abstracts.User;
import com.project.schoolmanagment.entity.concretes.business.LessonProgram;
import com.project.schoolmanagment.entity.concretes.business.StudentInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Teacher extends User {  // a kind of user
    //TODO: Orphan removal should be learned.
    // if you delete a teacher, advisory teacher will also be deleted.
    @JsonIgnore
    @OneToOne(mappedBy = "teacher", cascade= CascadeType.ALL, orphanRemoval = true)
    private AdvisoryTeacher advisoryTeacher;

    @Column(name = "isAdvisor")
    private Boolean isAdvisory;

    @Column(unique = true)
    private String email;

    // when we delete the teacher, student ainfo also will be deleted
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private List<StudentInfo>studentInfos;


    // when we have many to many , we need to have a new table with ids.
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "teacher_lessonprogram",
                joinColumns = @JoinColumn(name = "teacher_id"),
                inverseJoinColumns = @JoinColumn(name = "lesson_program_id"))
    private Set<LessonProgram>lessonsProgramList;
}
