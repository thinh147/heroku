package com.gogitek.toeictest.entity;

import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamEntity extends BaseEntity {
    @Column(name = "duration")
    private Long duration;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_type_id", referencedColumnName = "id")
    private ExamTypeEntity examType;

    @OneToMany(mappedBy = "examEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ExamQuestionEntity> examQuestionList;
}

