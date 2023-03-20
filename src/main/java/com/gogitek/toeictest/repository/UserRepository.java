package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT u from UserEntity u where u.username = :username or u.phoneNumber = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);
    Boolean existsByUsername(String username);

    @Query(value = "UPDATE UserEntity u SET u.level = com.gogitek.toeictest.constant.Level.TRIAL where u.id in (:userIds)")
    @Transactional
    @Modifying
    void updateUserLevel(@Param("userIds") List<Long> userIds);
}
