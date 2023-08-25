package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.concretes.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TeacherRepository extends JpaRepository<Teacher, Long>{

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);


}
