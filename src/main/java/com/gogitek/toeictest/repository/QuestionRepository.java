package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.constant.Part;
import com.gogitek.toeictest.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findByIdIn(Collection<Long> ids);

    @Query(value = "SELECT q from QuestionEntity q " +
            "left join q.examQuestionEntityList eq " +
            "left join eq.examEntity e " +
            "where e.id = :examId")
    Page<QuestionEntity> findByExamId(@Param("examId") Long examId, Pageable pageable);

    Page<QuestionEntity> findByPartIn(List<Part> parts, Pageable pageable);

    @Query(value = "SELECT q from QuestionEntity q ORDER BY RAND()")
    List<QuestionEntity> findRandomRecords(long quantity);

}
