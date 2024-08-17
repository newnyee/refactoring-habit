package com.refactoringhabit.review.domain.repository;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    Review findByAltIdAndMember(String altId, Member member);
}
