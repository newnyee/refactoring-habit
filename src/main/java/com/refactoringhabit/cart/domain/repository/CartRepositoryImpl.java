package com.refactoringhabit.cart.domain.repository;

import static com.refactoringhabit.cart.domain.entity.QCart.cart;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.cart.dto.CartDetailResponseDto;
import com.refactoringhabit.member.domain.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByMemberAndNotEqualsProductAltId(Member member, String productAltId) {
        return !jpaQueryFactory
            .selectFrom(cart)
            .where(cart.member.eq(member)
                .and(cart.option.product.altId.ne(productAltId)))
            .fetch().isEmpty();
    }

    @Override
    public List<CartDetailResponseDto> getCartDetailsByMember(Member member) {
        return jpaQueryFactory
            .select(Projections.constructor(CartDetailResponseDto.class,
                cart.altId.as("cartAltId"),
                cart.quantity.as("cartQuantity"),
                cart.option.product.altId.as("productAltId"),
                cart.option.product.name.as("productName"),
                cart.option.product.imageFileNames.as("productImages"),
                cart.option.altId.as("optionAltId"),
                cart.option.name.as("optionName"),
                cart.option.quantity.as("optionQuantity"),
                cart.option.price.as("optionPrice")))
            .from(cart)
            .where(cart.member.eq(member))
            .fetch();
    }
}
