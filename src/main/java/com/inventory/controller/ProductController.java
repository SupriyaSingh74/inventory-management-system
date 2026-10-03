package com.inventory.controller;

import com.inventory.dto.ProductDto;
import com.inventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductDto createProduct(
            @Valid @RequestBody ProductDto dto) {

        return service.createProduct(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public Page<ProductDto> getAllProducts(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "productName") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        return service.getAllProducts(
                page,
                size,
                sortBy,
                sortDir);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    public ProductDto getProductById(
            @PathVariable Long id) {

        return service.getProductById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductDto updateProduct(

            @PathVariable Long id,

            @Valid @RequestBody ProductDto dto) {

        return service.updateProduct(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        service.deleteProduct(id);

        return "Product Deleted Successfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/search")
    public Page<ProductDto> searchProduct(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        return service.searchProduct(keyword, page, size);
    }

}