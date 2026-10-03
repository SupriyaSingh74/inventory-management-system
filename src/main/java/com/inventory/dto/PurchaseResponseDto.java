package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDto {

    private Long purchaseId;

    private String invoiceNo;

    private String supplierName;

    private LocalDate purchaseDate;

    private BigDecimal totalAmount;

    private String status;

    private List<PurchaseItemResponseDto> items;

}