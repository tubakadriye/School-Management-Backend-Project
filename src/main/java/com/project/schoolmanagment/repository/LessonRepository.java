package com.project.schoolmanagment.repository;

import com.project.schoolmanagment.entity.concretes.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
}
