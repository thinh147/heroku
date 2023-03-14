package com.gogitek.toeictest.entity;

import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

@Entity
@Table(name = "exam_part")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamPartEntity extends BaseEntity {
    private String partName;
    private String description;
}
