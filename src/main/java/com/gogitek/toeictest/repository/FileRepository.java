package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
