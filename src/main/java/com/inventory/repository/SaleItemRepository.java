package com.inventory.repository;

import com.inventory.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleItemRepository
        extends JpaRepository<SaleItem, Long> {

}