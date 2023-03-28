package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.UserAnswerEntity;
import com.gogitek.toeictest.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswerEntity, Long> {
    List<UserAnswerEntity> findByUserEntity(UserEntity user);
}
