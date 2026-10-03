package com.inventory.service;

import com.inventory.dto.PurchaseDto;
import com.inventory.dto.PurchaseResponseDto;
import org.springframework.data.domain.Page;

public interface PurchaseService {

    PurchaseResponseDto createPurchase(PurchaseDto dto);

    Page<PurchaseResponseDto> getAllPurchases(
            int page,
            int size,
            String sortBy,
            String sortDir);

    PurchaseResponseDto getPurchaseById(Long id);

    PurchaseResponseDto getPurchaseByInvoice(String invoiceNo);

}