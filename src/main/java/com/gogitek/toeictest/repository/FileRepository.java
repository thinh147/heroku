package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    @Query(value = "SELECT f FROM FileEntity f where f.fileName like %:fileName%")
    List<FileEntity> findByFileNameLike(@Param("fileName") String fileName);
}
