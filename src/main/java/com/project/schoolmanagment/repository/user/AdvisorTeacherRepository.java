package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.concretes.business.Meet;
import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdvisorTeacherRepository extends JpaRepository<AdvisoryTeacher,Long> {
    Optional<AdvisoryTeacher>getAdvisoryTeacherByTeacher_Id(Long advisoryTeacherId);
    Optional<AdvisoryTeacher>findByTeacher_UsernameEquals(String username);

    @Override
    @Modifying
    @Query("DELETE FROM AdvisoryTeacher s WHERE s.id = :id")
    void deleteById(@Param("id") Long id);


}
