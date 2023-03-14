package com.gogitek.toeictest.mapper;

import com.gogitek.toeictest.controller.dto.response.VocabGroupResponse;
import com.gogitek.toeictest.controller.dto.response.VocabularyItemResponse;
import com.gogitek.toeictest.entity.VocabularyGroupEntity;
import com.gogitek.toeictest.entity.VocabularyItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VocabularyMapper {
    @Mapping(target = "items", source = "itemEntityList", qualifiedByName = "mapVocabItems")
    VocabGroupResponse entityToDto(VocabularyGroupEntity entity);

    @Named("mapVocabItems")
    default List<VocabularyItemResponse> mapVocab(List<VocabularyItemEntity> entity) {
        return entity.stream().map(item -> VocabularyItemResponse
                        .builder()
                        .word(item.getWord())
                        .audioPath(item.getAudioPath())
                        .description(item.getDescription())
                        .pronunciation(item.getPronunciation())
                        .build())
                .collect(Collectors.toList());
    }
}
