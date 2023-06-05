package com.project.schoolmanagment.repository;

import com.project.schoolmanagment.entity.concretes.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage,Long> {
}
