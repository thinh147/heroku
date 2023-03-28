package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.VocabularyItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabRepository extends JpaRepository<VocabularyItemEntity, Long> {
    @Query(value = "SELECT v FROM VocabularyItemEntity v LEFT JOIN v.group g " +
            "WHERE v.word LIKE %:word% " +
            "AND (:groupId IS NULL OR g.id = :groupId)")
    Page<VocabularyItemEntity> findByWordAndGroupId(@Param("word") String word,
                                                    @Param("groupId") Long groupId,
                                                    Pageable pageable);
}
