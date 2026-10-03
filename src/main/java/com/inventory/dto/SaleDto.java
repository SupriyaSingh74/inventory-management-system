package com.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SaleDto {

    private Long saleId;

    @NotNull
    private Long customerId;

    @Valid
    private List<SaleItemDto> items;
}