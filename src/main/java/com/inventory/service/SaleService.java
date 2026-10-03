package com.inventory.service;

import com.inventory.dto.SaleDto;
import com.inventory.dto.SaleResponseDto;
import org.springframework.data.domain.Page;

public interface SaleService {

    SaleResponseDto createSale(SaleDto dto);

    Page<SaleResponseDto> getAllSales(
            int page,
            int size,
            String sortBy,
            String sortDir);

    SaleResponseDto getSaleById(Long id);

    SaleResponseDto getSaleByInvoice(String invoiceNo);

}