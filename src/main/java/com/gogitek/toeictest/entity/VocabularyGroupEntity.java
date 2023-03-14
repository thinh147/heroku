package com.gogitek.toeictest.entity;


import com.gogitek.toeictest.entity.base.BaseEntity;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vocab_group")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VocabularyGroupEntity extends BaseEntity {
    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_image")
    private String groupImage;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VocabularyItemEntity> itemEntityList = new ArrayList<>();
}
