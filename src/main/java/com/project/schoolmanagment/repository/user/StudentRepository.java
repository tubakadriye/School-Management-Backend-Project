package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.entity.concretes.user.Student;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long > {

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Student findByUsernameEquals(String username);

    @Query(value = "SELECT (count (s)>0) from Student s") // checking if the count is bigger then 0, if tehre's any student
    boolean findStudent();

    @Query(value = "SELECT MAX (s.studentNumber) FROM Student s")
    int getMaxStudentNumber();

    List<Student> getStudentByNameContaining(String studentName);

    @Query(value = "SELECT s FROM Student s WHERE s.advisoryTeacher.teacher.username =:username")
    List<Student>getStudentByAdvisoryTeacher_Username(String username);

    @Query("SELECT s FROM Student  s WHERE s.id IN :id")
    List<Student>findByIdsEquals(Long [] id);


}
