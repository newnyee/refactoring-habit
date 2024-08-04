package com.refactoringhabit.category.domain.repository;

import com.refactoringhabit.category.domain.entity.CategoryLarge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryLargeRepository extends JpaRepository<CategoryLarge, Long>{
    CategoryLarge findByEngName(String engName);
}
