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
    date = "2024-07-29T15:13:32+0900",
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
        categoryMiddleList = categoryMiddleListToCategoryMiddleResponseDtoList( categoryLarge.getCategoryMiddles() );
        engName = categoryLarge.getEngName();
        image = categoryLarge.getImage();

        CategoryLargeResponseDto categoryLargeResponseDto = new CategoryLargeResponseDto( name, engName, image, categoryMiddleList );

        return categoryLargeResponseDto;
    }

    @Override
    public CategoryMiddleResponseDto toCategoryMiddleResponseDto(CategoryMiddle categoryMiddle) {
        if ( categoryMiddle == null ) {
            return null;
        }

        String altId = null;
        String name = null;

        altId = categoryMiddle.getAltId();
        name = categoryMiddle.getName();

        CategoryMiddleResponseDto categoryMiddleResponseDto = new CategoryMiddleResponseDto( altId, name );

        return categoryMiddleResponseDto;
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
