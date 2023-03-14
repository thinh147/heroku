package com.gogitek.toeictest.repository;

import com.gogitek.toeictest.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query(value = "SELECT p from PaymentEntity p where p.userEntity.phoneNumber like %:phoneNumber%")
    Page<PaymentEntity> retrievePaymentEntityByUserPhoneNumber(@Param("phoneNumber") String phoneNumber, Pageable pageable);
}
