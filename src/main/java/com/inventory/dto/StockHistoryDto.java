package com.inventory.dto;

import com.inventory.enums.StockType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockHistoryDto {

    private Long stockHistoryId;

    private Long productId;

    private String productName;

    private StockType stockType;

    private Integer quantity;

    private Integer previousStock;

    private Integer currentStock;

    private String remarks;

    private LocalDateTime createdAt;

}