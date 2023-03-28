package com.gogitek.toeictest.entity;

import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamQuestionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    private ExamEntity examEntity;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private QuestionEntity questionEntity;
}
