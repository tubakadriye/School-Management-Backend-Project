package com.project.schoolmanagment.repository.business;

import com.project.schoolmanagment.entity.concretes.business.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsLessonByLessonNameEqualsIgnoreCase(String lessonName);

    Optional<Lesson>getLessonByLessonName(String lessonName);

    //pay attantion of usage "lessonIds" parameter
    @Query(value = "SELECT l FROM Lesson l WHERE l.lessonId IN :lessonIds")
    Set<Lesson>getLessonByLessonIdIList(Set<Long> lessonIds);

    List<Lesson> getLessonsByCreditScoreGreaterThanEqual(Integer creditScore);
}
