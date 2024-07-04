package com.refactoringhabit.category.domain.mapper;

import com.refactoringhabit.category.domain.entity.CategoryLarge;
import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import com.refactoringhabit.category.dto.CategoryLargeResponseDto;
import com.refactoringhabit.category.dto.CategoryMiddleResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T15:37:48+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class CategoryEntityMapperImpl implements CategoryEntityMapper {

    @Override
    public List<CategoryLargeResponseDto> toCategoryLargeResponseDtoList(List<CategoryLarge> categoryLarges) {
        if ( categoryLarges == null ) {
            return null;
        }

        List<CategoryLargeResponseDto> list = new ArrayList<CategoryLargeResponseDto>( categoryLarges.size() );
        for ( CategoryLarge categoryLarge : categoryLarges ) {
            list.add( toCategoryLargeResponseDto( categoryLarge ) );
        }

        return list;
    }

    @Override
    public CategoryLargeResponseDto toCategoryLargeResponseDto(CategoryLarge categoryLarge) {
        if ( categoryLarge == null ) {
            return null;
        }

        CategoryLargeResponseDto.CategoryLargeResponseDtoBuilder categoryLargeResponseDto = CategoryLargeResponseDto.builder();

        categoryLargeResponseDto.name( categoryLarge.getName() );
        categoryLargeResponseDto.categoryMiddleList( categoryMiddleListToCategoryMiddleResponseDtoList( categoryLarge.getCategoryMiddles() ) );
        categoryLargeResponseDto.engName( categoryLarge.getEngName() );

        return categoryLargeResponseDto.build();
    }

    @Override
    public CategoryMiddleResponseDto toCategoryMiddleResponseDto(CategoryMiddle categoryMiddle) {
        if ( categoryMiddle == null ) {
            return null;
        }

        CategoryMiddleResponseDto.CategoryMiddleResponseDtoBuilder categoryMiddleResponseDto = CategoryMiddleResponseDto.builder();

        categoryMiddleResponseDto.altId( categoryMiddle.getAltId() );
        categoryMiddleResponseDto.name( categoryMiddle.getName() );

        return categoryMiddleResponseDto.build();
    }

    protected List<CategoryMiddleResponseDto> categoryMiddleListToCategoryMiddleResponseDtoList(List<CategoryMiddle> list) {
        if ( list == null ) {
            return null;
        }

        List<CategoryMiddleResponseDto> list1 = new ArrayList<CategoryMiddleResponseDto>( list.size() );
        for ( CategoryMiddle categoryMiddle : list ) {
            list1.add( toCategoryMiddleResponseDto( categoryMiddle ) );
        }

        return list1;
    }
}
