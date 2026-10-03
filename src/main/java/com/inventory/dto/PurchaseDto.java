package com.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseDto {

    private Long purchaseId;

    @NotNull(message = "Supplier Id is required")
    private Long supplierId;

    @Valid
    private List<PurchaseItemDto> items;
}