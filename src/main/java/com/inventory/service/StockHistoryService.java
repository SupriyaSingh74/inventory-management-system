package com.inventory.service;

import com.inventory.dto.StockHistoryDto;

import java.util.List;

public interface StockHistoryService {

    StockHistoryDto stockIn(Long productId,
                            Integer quantity,
                            String remarks);

    StockHistoryDto stockOut(Long productId,
                             Integer quantity,
                             String remarks);

    StockHistoryDto adjustStock(Long productId,
                                Integer quantity,
                                String remarks);

    List<StockHistoryDto> getStockHistory(Long productId);

}