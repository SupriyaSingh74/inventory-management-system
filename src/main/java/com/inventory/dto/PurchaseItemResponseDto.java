package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemResponseDto {

    private Long productId;

    private String productName;

    private Integer quantity;

    private BigDecimal purchasePrice;

    private BigDecimal total;

}