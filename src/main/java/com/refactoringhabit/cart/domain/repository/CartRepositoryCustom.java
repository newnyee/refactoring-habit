package com.refactoringhabit.cart.domain.repository;

import com.refactoringhabit.cart.dto.CartDetailResponseDto;
import com.refactoringhabit.member.domain.entity.Member;
import java.util.List;

public interface CartRepositoryCustom {
    boolean existsByMemberAndNotEqualsProductAltId(Member member, String productAltId);
    List<CartDetailResponseDto> getCartDetailsByMember(Member member);
}
