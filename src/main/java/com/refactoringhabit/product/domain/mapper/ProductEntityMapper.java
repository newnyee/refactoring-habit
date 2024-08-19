package com.refactoringhabit.product.domain.mapper;

import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.dto.HostProductInfoDto;
import com.refactoringhabit.host.dto.SimpleHostInfoDto;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.dto.OptionDetailDto;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.ProductResponseDto;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductEntityMapper {

    ProductEntityMapper INSTANCE = Mappers.getMapper(ProductEntityMapper.class);

    @Mapping(target = "altId", source = "productAltId", qualifiedByName = "generateUuid")
    @Mapping(target = "name", source = "dto.productName")
    Product toEntity(HostProductInfoDto dto, String productAltId, String imageFileNames,
        CategoryMiddle categoryMiddle, Host host);

    ProductResponseDto toProductResponseDto(ProductDetailDto productDetailDto,
        List<OptionDetailDto> optionDetailDtos, SimpleHostInfoDto simpleHostInfoDto,
        List<ReviewDetailDto> reviewDetailDtos, String wishAltId);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
