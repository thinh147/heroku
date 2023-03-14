package com.gogitek.toeictest.entity;

import com.gogitek.toeictest.constant.PaymentStatus;
import com.gogitek.toeictest.constant.Period;
import com.gogitek.toeictest.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payment_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity extends BaseEntity {
    @Column(name = "period")
    @Enumerated(EnumType.STRING)
    private Period period;

    @Column(name = "cost")
    private Double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private UserEntity adminEntity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
