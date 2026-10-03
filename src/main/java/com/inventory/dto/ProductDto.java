package com.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "SKU is required")
    private String sku;

    private String barcode;

    private String description;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal sellingPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 0)
    private Integer quantity;

    @NotNull(message = "Minimum stock is required")
    @Min(value = 0)
    private Integer minimumStock;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Supplier is required")
    private Long supplierId;

}