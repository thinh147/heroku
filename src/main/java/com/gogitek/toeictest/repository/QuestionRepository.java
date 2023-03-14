package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findByIdIn(List<Long> ids);

    @Query(value = "SELECT q from QuestionEntity q " +
            "left join q.examQuestionEntityList eq " +
            "left join eq.examEntity e " +
            "left join e.examType et " +
            "where et.id = :typeId")
    Page<QuestionEntity> findByExamTypeId(@Param("typeId") Long typeId, Pageable pageable);
}
