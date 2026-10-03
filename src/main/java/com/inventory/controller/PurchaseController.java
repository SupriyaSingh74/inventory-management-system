package com.inventory.controller;

import com.inventory.dto.PurchaseDto;
import com.inventory.dto.PurchaseResponseDto;
import com.inventory.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PurchaseResponseDto createPurchase(
            @Valid @RequestBody PurchaseDto dto) {

        return purchaseService.createPurchase(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public Page<PurchaseResponseDto> getAllPurchases(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "purchaseDate") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir) {

        return purchaseService.getAllPurchases(
                page,
                size,
                sortBy,
                sortDir);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    public PurchaseResponseDto getPurchaseById(
            @PathVariable Long id) {

        return purchaseService.getPurchaseById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/invoice/{invoiceNo}")
    public PurchaseResponseDto getPurchaseByInvoice(
            @PathVariable String invoiceNo) {

        return purchaseService.getPurchaseByInvoice(invoiceNo);
    }

}