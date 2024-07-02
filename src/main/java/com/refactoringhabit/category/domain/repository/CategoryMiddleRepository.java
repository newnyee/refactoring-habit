package com.refactoringhabit.category.domain.repository;

import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMiddleRepository extends JpaRepository<CategoryMiddle, Long> {
    Optional<CategoryMiddle> findByAltId(String altId);
}
