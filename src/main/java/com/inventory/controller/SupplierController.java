package com.inventory.controller;

import com.inventory.dto.SupplierDto;
import com.inventory.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SupplierDto createSupplier(
            @Valid @RequestBody SupplierDto dto) {

        return service.createSupplier(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public Page<SupplierDto> getAllSuppliers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "supplierName")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDir) {

        return service.getAllSuppliers(
                page,
                size,
                sortBy,
                sortDir);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    public SupplierDto getSupplier(@PathVariable Long id) {

        return service.getSupplierById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SupplierDto updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierDto dto) {

        return service.updateSupplier(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteSupplier(@PathVariable Long id) {

        service.deleteSupplier(id);

        return "Supplier Deleted Successfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/search")
    public Page<SupplierDto> searchSupplier(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        return service.searchSupplier(keyword, page, size);
    }
}