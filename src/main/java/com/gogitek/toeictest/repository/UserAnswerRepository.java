package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.UserAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswerEntity, Long> {
}
