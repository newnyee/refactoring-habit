package com.refactoringhabit.cart.domain.mapper;

import com.refactoringhabit.cart.domain.entity.Cart;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Option;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartEntityMapper {

    CartEntityMapper INSTANCE = Mappers.getMapper(CartEntityMapper.class);

    @Mapping(target = "option", source = "option")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "altId", source = "altId", qualifiedByName = "generateUuid")
    Cart toEntity(Member member, Option option, int quantity, String altId);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
