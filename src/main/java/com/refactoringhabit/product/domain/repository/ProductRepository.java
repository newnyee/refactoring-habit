package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
