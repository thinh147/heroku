package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.ExamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    @Query(value = "SELECT e from ExamEntity e where e.examType.id = :typeId")
    Page<ExamEntity> findByExamTypeId(@Param("typeId") Long typeId, Pageable pageable);
}
