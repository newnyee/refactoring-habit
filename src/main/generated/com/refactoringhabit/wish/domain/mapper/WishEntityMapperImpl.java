package com.refactoringhabit.wish.domain.mapper;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.wish.domain.entity.Wish;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-10T11:20:48+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class WishEntityMapperImpl implements WishEntityMapper {

    @Override
    public Wish toEntity(Member member, Product product, String wishAltId) {
        if ( member == null && product == null && wishAltId == null ) {
            return null;
        }

        Wish.WishBuilder wish = Wish.builder();

        wish.member( member );
        wish.product( product );
        wish.altId( generateUuid( wishAltId ) );

        return wish.build();
    }
}
