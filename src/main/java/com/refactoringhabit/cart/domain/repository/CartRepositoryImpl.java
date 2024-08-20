package com.refactoringhabit.cart.domain.repository;

import static com.refactoringhabit.cart.domain.entity.QCart.cart;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.member.domain.entity.Member;
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
}
