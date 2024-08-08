package com.refactoringhabit.category.domain.repository;

import com.refactoringhabit.category.domain.entity.CategoryLarge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryLargeRepository extends JpaRepository<CategoryLarge, Long>{
    @Query("SELECT cl FROM CategoryLarge cl LEFT JOIN FETCH cl.categoryMiddles WHERE cl.engName = :engName")
    CategoryLarge findByEngName(@Param("engName") String engName);

    @Query("SELECT cl FROM CategoryLarge cl LEFT JOIN FETCH cl.categoryMiddles")
    List<CategoryLarge> findAllWithCategoryMiddles();
}
