package com.project.schoolmanagment.repository;

import com.project.schoolmanagment.entity.concretes.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

	boolean existsLessonByLessonNameEqualsIgnoreCase(String lessonName);


	Optional<Lesson>getLessonByLessonName(String lessonName);
}
