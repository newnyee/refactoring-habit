package com.refactoringhabit.wish.domain.repository;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import java.util.Optional;

public interface WishRepositoryCustom {
    Optional<String> findAltIdByMemberAndProduct(Member member, Product product);
}
