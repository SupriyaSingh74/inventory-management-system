package com.inventory.service.impl;

import com.inventory.dto.DashboardAnalyticsDto;
import com.inventory.dto.DashboardResponseDto;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.CustomerRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.PurchaseRepository;
import com.inventory.repository.SaleRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final SupplierRepository supplierRepository;

    private final CustomerRepository customerRepository;

    private final PurchaseRepository purchaseRepository;

    private final SaleRepository saleRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Override
    public DashboardResponseDto getDashboard() {

        logger.info("Fetching Dashboard Details");

        DashboardResponseDto dto = new DashboardResponseDto();

        dto.setTotalProducts(productRepository.count());
        dto.setTotalCategories(categoryRepository.count());
        dto.setTotalSuppliers(supplierRepository.count());
        dto.setTotalCustomers(customerRepository.count());
        dto.setTotalPurchases(purchaseRepository.count());
        dto.setTotalSales(saleRepository.count());

        logger.info(
                "Dashboard Counts -> Products: {}, Categories: {}, Suppliers: {}, Customers: {}, Purchases: {}, Sales: {}",
                dto.getTotalProducts(),
                dto.getTotalCategories(),
                dto.getTotalSuppliers(),
                dto.getTotalCustomers(),
                dto.getTotalPurchases(),
                dto.getTotalSales());

        // Low Stock Products (Quantity <= 10)
        dto.setLowStockProducts(
                productRepository.countByQuantityLessThanEqual(10));

        // Out Of Stock Products (Quantity = 0)
        dto.setOutOfStockProducts(
                productRepository.countByQuantity(0));

        logger.info(
                "Inventory Status -> Low Stock: {}, Out Of Stock: {}",
                dto.getLowStockProducts(),
                dto.getOutOfStockProducts());

        DashboardAnalyticsDto analytics =
                new DashboardAnalyticsDto();

        analytics.setTodayPurchaseAmount(
                purchaseRepository.getTodayPurchaseAmount(LocalDate.now()));

        analytics.setTodaySalesAmount(
                saleRepository.getTodaySalesAmount(LocalDate.now()));

        analytics.setTodayPurchaseCount(
                purchaseRepository.countByPurchaseDate(LocalDate.now()));

        analytics.setTodaySalesCount(
                saleRepository.countBySaleDate(LocalDate.now()));

        logger.info(
                "Today's Analytics -> Purchase Amount: {}, Sales Amount: {}, Purchase Count: {}, Sales Count: {}",
                analytics.getTodayPurchaseAmount(),
                analytics.getTodaySalesAmount(),
                analytics.getTodayPurchaseCount(),
                analytics.getTodaySalesCount());

        dto.setAnalytics(analytics);

        logger.info("Dashboard Data Fetched Successfully");

        return dto;
    }
}