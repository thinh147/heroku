package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
}
