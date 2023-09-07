package com.project.schoolmanagment.repository.business;

import com.project.schoolmanagment.entity.concretes.business.StudentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentInfoRepository  extends JpaRepository<StudentInfo, Long> {
    List<StudentInfo> getAllByStudentId_Id(Long studentId);


    @Query("SELECT s FROM StudentInfo  s WHERE s.teacher.username =? 1") // 1 is first parameter, username
    Page<StudentInfo> findByTeacherId_UsernameEquals(String username, Pageable pageable);

    @Query("SELECT s FROM StudentInfo  s WHERE s.student.username =? 1")
    Page<StudentInfo> findByStudentId_UsernameEquals(String username, Pageable pageable);


}
