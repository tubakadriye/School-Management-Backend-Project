package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdvisorTeacherRepository extends JpaRepository<AdvisoryTeacher,Long> {
    Optional<AdvisoryTeacher>getAdvisoryTeacherByTeacher_Id(Long advisoryTeacherId);


}
