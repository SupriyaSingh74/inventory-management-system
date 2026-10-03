package com.inventory.service;

import com.inventory.dto.ProductDto;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductDto createProduct(ProductDto dto);

    Page<ProductDto> getAllProducts(
            int page,
            int size,
            String sortBy,
            String sortDir);

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductDto dto);

    void deleteProduct(Long id);

    Page<ProductDto> searchProduct(
            String keyword,
            int page,
            int size);

}