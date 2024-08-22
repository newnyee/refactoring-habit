package com.refactoringhabit.wish.domain.repository;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.wish.domain.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long>, WishRepositoryCustom {
    Long countByProductId(Long productId);
    void deleteByMemberAndProductAndAltId(Member member, Product product, String altId);
    Long countByMember(Member member);
}
