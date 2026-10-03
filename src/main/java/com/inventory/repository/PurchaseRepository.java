package com.inventory.repository;

import com.inventory.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByInvoiceNo(String invoiceNo);

    Page<Purchase> findAll(Pageable pageable);

    Page<Purchase> findByPurchaseDate(
            LocalDate purchaseDate,
            Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(p.totalAmount),0)
    FROM Purchase p
    WHERE p.purchaseDate = :date
    """)
    BigDecimal getTodayPurchaseAmount(@Param("date") LocalDate date);

    long countByPurchaseDate(LocalDate date);

}