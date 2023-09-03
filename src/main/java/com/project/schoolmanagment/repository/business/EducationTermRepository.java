package com.project.schoolmanagment.repository.business;

import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.entity.enums.Term;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Repository
public interface EducationTermRepository extends JpaRepository<EducationTerm,Long> {

    @Query("SELECT (count (e) >0 ) FROM EducationTerm e WHERE e.term = ?1 AND EXTRACT(YEAR FROM e.startDate) = ?2 ")
    boolean existsByTermAndYear(Term term, int year);


}
