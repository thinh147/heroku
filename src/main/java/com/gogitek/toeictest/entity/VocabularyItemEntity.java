package com.gogitek.toeictest.entity;

import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "vocab_item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VocabularyItemEntity extends BaseEntity {
    @Column(name = "word")
    private String word;

    @Column(name = "pronunciation")
    private String pronunciation;

    @Column(name = "audio")
    private String audioPath;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private VocabularyGroupEntity group;
}
