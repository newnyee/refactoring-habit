package com.refactoringhabit.host.domain.repository;

import com.refactoringhabit.host.dto.SimpleHostInfoDto;

public interface HostRepositoryCustom {
    SimpleHostInfoDto getSimpleHostInfoById(Long id);
}
