package com.inventory.repository;

import com.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    Page<Product> findByProductNameContainingIgnoreCase(
            String keyword,
            Pageable pageable);

    long countByQuantityLessThanEqual(Integer quantity);

    long countByQuantity(Integer quantity);

}