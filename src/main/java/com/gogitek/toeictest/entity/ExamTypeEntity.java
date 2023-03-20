package com.gogitek.toeictest.entity;


import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exam_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamTypeEntity extends BaseEntity {
    @Column(name = "exam_type_description")
    private String description;

    @Column(name = "question_quantity")
    private Long quantity;

    @OneToMany(mappedBy = "examType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ExamEntity> exams = new ArrayList<>();
}
