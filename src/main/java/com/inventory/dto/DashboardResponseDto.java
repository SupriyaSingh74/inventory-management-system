package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDto {

    private Long totalProducts;

    private Long totalCategories;

    private Long totalSuppliers;

    private Long totalCustomers;

    private Long totalPurchases;

    private Long totalSales;

    private Long lowStockProducts;

    private Long outOfStockProducts;

    private DashboardAnalyticsDto analytics;

}