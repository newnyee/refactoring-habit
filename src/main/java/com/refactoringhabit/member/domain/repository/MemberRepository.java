package com.refactoringhabit.member.domain.repository;

import com.refactoringhabit.member.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Boolean existsByAltId(String altId);
}
