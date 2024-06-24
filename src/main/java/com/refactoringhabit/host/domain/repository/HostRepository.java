package com.refactoringhabit.host.domain.repository;

import com.refactoringhabit.host.domain.entity.Host;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
    Boolean existsByNickName(String nickName);
    Optional<Host> findByAltId(String hostAltId);
}
