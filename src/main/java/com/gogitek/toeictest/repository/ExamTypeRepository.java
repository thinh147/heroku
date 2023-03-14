package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.ExamTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamTypeRepository extends JpaRepository<ExamTypeEntity, Long> {
}
