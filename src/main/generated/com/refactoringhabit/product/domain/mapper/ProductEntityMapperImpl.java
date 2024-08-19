package com.refactoringhabit.product.domain.mapper;

import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.dto.HostProductInfoDto;
import com.refactoringhabit.host.dto.SimpleHostInfoDto;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.enums.ProductType;
import com.refactoringhabit.product.dto.OptionDetailDto;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.ProductResponseDto;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-17T16:37:13+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class ProductEntityMapperImpl implements ProductEntityMapper {

    @Override
    public Product toEntity(HostProductInfoDto dto, String productAltId, String imageFileNames, CategoryMiddle categoryMiddle, Host host) {
        if ( dto == null && productAltId == null && imageFileNames == null && categoryMiddle == null && host == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        if ( dto != null ) {
            product.name( dto.getProductName() );
            product.zipCode( dto.getZipCode() );
            product.address1( dto.getAddress1() );
            product.address2( dto.getAddress2() );
            product.extraAddress( dto.getExtraAddress() );
            product.description( dto.getDescription() );
            product.closedAt( dto.getClosedAt() );
            product.tagGender( dto.getTagGender() );
            product.tagAge( dto.getTagAge() );
            product.tagWith( dto.getTagWith() );
            if ( dto.getType() != null ) {
                product.type( Enum.valueOf( ProductType.class, dto.getType() ) );
            }
        }
        product.altId( generateUuid( productAltId ) );
        product.imageFileNames( imageFileNames );
        product.categoryMiddle( categoryMiddle );
        product.host( host );

        return product.build();
    }

    @Override
    public ProductResponseDto toProductResponseDto(ProductDetailDto productDetailDto, List<OptionDetailDto> optionDetailDtos, SimpleHostInfoDto simpleHostInfoDto, List<ReviewDetailDto> reviewDetailDtos, String wishAltId) {
        if ( productDetailDto == null && optionDetailDtos == null && simpleHostInfoDto == null && reviewDetailDtos == null && wishAltId == null ) {
            return null;
        }

        ProductResponseDto.ProductResponseDtoBuilder productResponseDto = ProductResponseDto.builder();

        productResponseDto.productDetailDto( productDetailDto );
        List<OptionDetailDto> list = optionDetailDtos;
        if ( list != null ) {
            productResponseDto.optionDetailDtos( new ArrayList<OptionDetailDto>( list ) );
        }
        productResponseDto.simpleHostInfoDto( simpleHostInfoDto );
        List<ReviewDetailDto> list1 = reviewDetailDtos;
        if ( list1 != null ) {
            productResponseDto.reviewDetailDtos( new ArrayList<ReviewDetailDto>( list1 ) );
        }
        productResponseDto.wishAltId( wishAltId );

        return productResponseDto.build();
    }
}
