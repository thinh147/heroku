package com.gogitek.toeictest.mapper;

import com.gogitek.toeictest.controller.dto.response.UserAdminResponse;
import com.gogitek.toeictest.controller.dto.response.UserProfiles;
import com.gogitek.toeictest.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", source = "lastName")
    UserProfiles entityToResponse(UserEntity entity);

    @Mapping(target = "fullName", source = "lastName")
    UserAdminResponse entityToAdminResponse(UserEntity user);
}
