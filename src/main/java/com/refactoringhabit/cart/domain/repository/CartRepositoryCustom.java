package com.refactoringhabit.cart.domain.repository;

import com.refactoringhabit.member.domain.entity.Member;

public interface CartRepositoryCustom {
    boolean existsByMemberAndNotEqualsProductAltId(Member member, String productAltId);
}
