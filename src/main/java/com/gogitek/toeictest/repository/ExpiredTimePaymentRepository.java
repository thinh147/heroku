package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.ExpiredTimePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpiredTimePaymentRepository extends JpaRepository<ExpiredTimePayment, Long> {
    @Query(value = "SELECT et from ExpiredTimePayment et where et.expiredDate < :expired")
    List<ExpiredTimePayment> findByExpiredTime(@Param("expired") LocalDateTime expired);
}
