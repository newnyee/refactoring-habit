package com.refactoringhabit.wish.domain.repository;

import com.refactoringhabit.wish.domain.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long>, WishRepositoryCustom {
    Long countByProductId(Long productId);
}
