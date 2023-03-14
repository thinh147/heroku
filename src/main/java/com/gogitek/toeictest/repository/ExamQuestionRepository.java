package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.ExamQuestionEntity;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestionEntity, Long> {
    @Query(value = "UPDATE ExamQuestionEntity e SET e.examEntity.id = :examId, e.questionEntity.id = :questionId " +
            "WHERE e.examEntity = NULL AND e.questionEntity = NULL")
    @Modifying
    @Transactional
    void insertNewRecord(Long examId, Long questionId);
}
