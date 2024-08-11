package com.refactoringhabit.wish.domain.mapper;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.wish.domain.entity.Wish;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WishEntityMapper {

    WishEntityMapper INSTANCE = Mappers.getMapper(WishEntityMapper.class);

    @Mapping(target = "altId", source = "wishAltId", qualifiedByName = "generateUuid")
    Wish toEntity(Member member, Product product, String wishAltId);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
