package com.gogitek.toeictest.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "expired_time_payment")
public class ExpiredTimePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @Column(name = "user_id")
    private Long userId;
}
