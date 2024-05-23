package com.refactoringhabit.member.domain.repository;

import com.refactoringhabit.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Boolean existsByEmail(String email);
}
