package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.domain.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

}
