package com.refactoringhabit.wish.domain.repository;

import static com.refactoringhabit.wish.domain.entity.QWish.wish;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WishRepositoryImpl implements WishRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<String> findAltIdByMemberAndProduct(Member member, Product product) {
        return Optional.ofNullable(
            jpaQueryFactory
            .select(wish.altId)
            .from(wish)
            .where(wish.member.eq(member).and(wish.product.eq(product)))
            .fetchOne());
    }
}
