package com.refactoringhabit.member.domain.repository;

import static com.refactoringhabit.member.domain.entity.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<String> findEmailByPhoneAndBirth(FindEmailRequestDto findEmailRequestDto) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(member.email)
                .from(member)
                .where(member.phone.eq(findEmailRequestDto.getPhone())
                    .and(member.birth.eq(findEmailRequestDto.getBirth())))
                .fetchOne());
    }
}
