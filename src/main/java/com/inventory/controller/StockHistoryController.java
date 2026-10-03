package com.inventory.controller;

import com.inventory.dto.StockHistoryDto;
import com.inventory.dto.StockRequest;
import com.inventory.service.StockHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    @PostMapping("/in")
    public StockHistoryDto stockIn(
            @Valid @RequestBody StockRequest request) {

        return stockHistoryService.stockIn(
                request.getProductId(),
                request.getQuantity(),
                request.getRemarks());
    }

    @PostMapping("/out")
    public StockHistoryDto stockOut(
            @Valid @RequestBody StockRequest request) {

        return stockHistoryService.stockOut(
                request.getProductId(),
                request.getQuantity(),
                request.getRemarks());
    }

    @PostMapping("/adjust")
    public StockHistoryDto adjustStock(
            @Valid @RequestBody StockRequest request) {

        return stockHistoryService.adjustStock(
                request.getProductId(),
                request.getQuantity(),
                request.getRemarks());
    }

    @GetMapping("/history/{productId}")
    public List<StockHistoryDto> getHistory(
            @PathVariable Long productId) {

        return stockHistoryService.getStockHistory(productId);
    }
}