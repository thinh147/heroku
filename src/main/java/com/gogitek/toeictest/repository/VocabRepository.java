package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.VocabularyItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabRepository extends JpaRepository<VocabularyItemEntity, Long> {
}
