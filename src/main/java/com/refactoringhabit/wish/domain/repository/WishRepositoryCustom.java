package com.refactoringhabit.wish.domain.repository;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.dto.ProductCardDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface WishRepositoryCustom {
    Optional<String> findAltIdByMemberAndProduct(Member member, Product product);
    List<ProductCardDto> getWishesByMember(Member member, Pageable pageable);
}
