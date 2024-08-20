package com.refactoringhabit.cart.domain.mapper;

import com.refactoringhabit.cart.domain.entity.Cart;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Option;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-19T14:57:09+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class CartEntityMapperImpl implements CartEntityMapper {

    @Override
    public Cart toEntity(Member member, Option option, int quantity, String altId) {
        if ( member == null && option == null && altId == null ) {
            return null;
        }

        Cart.CartBuilder cart = Cart.builder();

        cart.member( member );
        cart.option( option );
        cart.quantity( quantity );
        cart.altId( generateUuid( altId ) );

        return cart.build();
    }
}
