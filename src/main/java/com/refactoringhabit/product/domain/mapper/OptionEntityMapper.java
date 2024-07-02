package com.refactoringhabit.product.domain.mapper;

import com.refactoringhabit.host.dto.HostOptionInfoDto;
import com.refactoringhabit.product.domain.entity.Option;
import com.refactoringhabit.product.domain.entity.Product;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionEntityMapper {

    OptionEntityMapper INSTANCE = Mappers.getMapper(OptionEntityMapper.class);

    @Mapping(target = "altId", source = "optionAltId", qualifiedByName = "generateUuid")
    @Mapping(target = "name", source = "dto.name")
    Option toEntity(HostOptionInfoDto dto, String optionAltId, Product product);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
