package com.refactoringhabit.cart.domain.repository;

import com.refactoringhabit.cart.domain.entity.Cart;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {
    void deleteByMember(Member member);
    Cart findByMemberAndOption(Member member, Option option);
}
