package com.refactoringhabit.category.domain.mapper;

import com.refactoringhabit.category.domain.entity.CategoryLarge;
import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import com.refactoringhabit.category.dto.CategoryMiddleResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryEntityMapper {

    CategoryEntityMapper INSTANCE = Mappers.getMapper(CategoryEntityMapper.class);

    List<CategoryLargeResponseDto> toCategoryLargeResponseDtoList(List<CategoryLarge> categoryLarges);

    @Mapping(target = "name", source = "categoryLarge.name")
    @Mapping(target = "categoryMiddleList", source = "categoryLarge.categoryMiddles")
    CategoryLargeResponseDto toCategoryLargeResponseDto(CategoryLarge categoryLarge);

    CategoryMiddleResponseDto toCategoryMiddleResponseDto(CategoryMiddle categoryMiddle);
}
