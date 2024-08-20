package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.domain.entity.Option;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long>, OptionRepositoryCustom {
    Optional<Option> findByAltId(String altId);
}
