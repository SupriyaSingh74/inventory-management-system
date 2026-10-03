package com.inventory.controller;

import com.inventory.dto.SaleDto;
import com.inventory.dto.SaleResponseDto;
import com.inventory.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PostMapping
    public SaleResponseDto createSale(
            @Valid @RequestBody SaleDto dto) {

        return saleService.createSale(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public Page<SaleResponseDto> getAllSales(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "saleDate") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir) {

        return saleService.getAllSales(
                page,
                size,
                sortBy,
                sortDir);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    public SaleResponseDto getSaleById(
            @PathVariable Long id) {

        return saleService.getSaleById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/invoice/{invoiceNo}")
    public SaleResponseDto getSaleByInvoice(
            @PathVariable String invoiceNo) {

        return saleService.getSaleByInvoice(invoiceNo);
    }

}