package com.gogitek.toeictest.mapper;

import com.gogitek.toeictest.controller.dto.response.PaymentResponse;
import com.gogitek.toeictest.controller.dto.response.UserPaymentResponse;
import com.gogitek.toeictest.entity.PaymentEntity;
import com.gogitek.toeictest.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "userInfo", source = "userEntity", qualifiedByName = "mapUserInfo")
    PaymentResponse entityToDto(PaymentEntity entity);

    @Named("mapUserInfo")
    default UserPaymentResponse mapUserInfo(UserEntity userEntity) {
        return UserPaymentResponse
                .builder()
                .userId(userEntity.getId())
                .name(userEntity.getLastName())
                .build();
    }
}
