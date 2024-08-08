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
    date = "2024-08-08T10:45:24+0900",
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

        String name = null;
        List<CategoryMiddleResponseDto> categoryMiddleList = null;
        String engName = null;
        String image = null;

        name = categoryLarge.getName();
        categoryMiddleList = toCategoryMiddleResponseDtoList( categoryLarge.getCategoryMiddles() );
        engName = categoryLarge.getEngName();
        image = categoryLarge.getImage();

        CategoryLargeResponseDto categoryLargeResponseDto = new CategoryLargeResponseDto( name, engName, image, categoryMiddleList );

        return categoryLargeResponseDto;
    }

    @Override
    public List<CategoryMiddleResponseDto> toCategoryMiddleResponseDtoList(List<CategoryMiddle> categoryMiddles) {
        if ( categoryMiddles == null ) {
            return null;
        }

        List<CategoryMiddleResponseDto> list = new ArrayList<CategoryMiddleResponseDto>( categoryMiddles.size() );
        for ( CategoryMiddle categoryMiddle : categoryMiddles ) {
            list.add( toCategoryMiddleResponseDto( categoryMiddle ) );
        }

        return list;
    }

    @Override
    public CategoryMiddleResponseDto toCategoryMiddleResponseDto(CategoryMiddle categoryMiddle) {
        if ( categoryMiddle == null ) {
            return null;
        }

        String altId = null;
        String name = null;
        String engName = null;

        altId = categoryMiddle.getAltId();
        name = categoryMiddle.getName();
        engName = categoryMiddle.getEngName();

        CategoryMiddleResponseDto categoryMiddleResponseDto = new CategoryMiddleResponseDto( altId, name, engName );

        return categoryMiddleResponseDto;
    }
}
