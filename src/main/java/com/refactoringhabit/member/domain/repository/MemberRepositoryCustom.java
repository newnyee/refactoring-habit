package com.refactoringhabit.member.domain.repository;

import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<String> findEmailByPhoneAndBirth(FindEmailRequestDto findEmailRequestDto);
}
