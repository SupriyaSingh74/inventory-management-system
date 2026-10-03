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
public class SaleResponseDto {

    private Long saleId;

    private String invoiceNo;

    private String customerName;

    private LocalDate saleDate;

    private BigDecimal totalAmount;

    private String status;

    private List<SaleItemResponseDto> items;

}