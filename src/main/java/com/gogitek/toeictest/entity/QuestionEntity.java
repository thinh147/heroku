package com.gogitek.toeictest.entity;

import com.gogitek.toeictest.constant.Part;
import com.gogitek.toeictest.constant.QuestionType;
import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType type;

    @Column(name = "question_detail", columnDefinition = "TEXT")
    private String detail;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "media_path", columnDefinition = "TEXT")
    private String mediaPath;

    @Column(name = "choiceA")
    private String choiceA;

    @Column(name = "choiceB")
    private String choiceB;


    @Column(name = "choiceC")
    private String choiceC;

    @Column(name = "choiceD")
    private String choiceD;

    @Column(name = "true_answer")
    private String trueAnswer;

    @Column(name = "part")
    @Enumerated(EnumType.STRING)
    private Part part;

    @OneToMany(mappedBy = "questionEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ExamQuestionEntity> examQuestionEntityList;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
