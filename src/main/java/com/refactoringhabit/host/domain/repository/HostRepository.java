package com.refactoringhabit.host.domain.repository;

import com.refactoringhabit.host.domain.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {

    Boolean existsByNickName(String nickName);
}
