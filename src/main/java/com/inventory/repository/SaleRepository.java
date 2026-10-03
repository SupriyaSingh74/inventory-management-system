package com.inventory.repository;

import com.inventory.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface SaleRepository
        extends JpaRepository<Sale, Long> {

    Optional<Sale> findByInvoiceNo(String invoiceNo);

    @Query("""
    SELECT COALESCE(SUM(s.totalAmount),0)
    FROM Sale s
    WHERE s.saleDate = :date
    """)
    BigDecimal getTodaySalesAmount(@Param("date") LocalDate date);

    long countBySaleDate(LocalDate date);

}