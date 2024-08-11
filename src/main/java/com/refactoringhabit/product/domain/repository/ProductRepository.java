package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.domain.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Long countByHostId(Long hostId);
    Optional<Product> findByAltId(String altId);
}
