package com.gogitek.toeictest.entity;

import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_answer_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAnswerEntity extends BaseEntity {
    @Column(name = "list_answer", columnDefinition = "TEXT")
    private String listAnswer;

    @Column(name = "mark")
    private Double mark;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private UserEntity userEntity;

    @Column(name = "exam_id")
    private Long examId;
}
