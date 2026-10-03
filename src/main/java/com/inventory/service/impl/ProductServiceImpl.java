package com.inventory.service.impl;

import com.inventory.dto.ProductDto;
import com.inventory.entity.Category;
import com.inventory.entity.Product;
import com.inventory.entity.Supplier;
import com.inventory.exception.DuplicateResourceException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ModelMapper mapper;

    private static final Logger logger =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto createProduct(ProductDto dto) {

        logger.info("Creating Product : {}", dto.getProductName());

        if (productRepository.existsBySku(dto.getSku())) {

            logger.error("SKU Already Exists : {}", dto.getSku());

            throw new DuplicateResourceException("SKU already exists");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> {

                    logger.error("Category Not Found : {}", dto.getCategoryId());

                    return new ResourceNotFoundException("Category not found");
                });

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> {

                    logger.error("Supplier Not Found : {}", dto.getSupplierId());

                    return new ResourceNotFoundException("Supplier not found");
                });

        Product product = mapper.map(dto, Product.class);

        product.setCategory(category);
        product.setSupplier(supplier);

        Product saved = productRepository.save(product);

        logger.info("Product Created Successfully : {}", saved.getProductId());

        return mapper.map(saved, ProductDto.class);
    }

    @Override
    public Page<ProductDto> getAllProducts(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching All Products | Page : {} | Size : {} | SortBy : {} | Direction : {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        logger.info("Total Products Found : {}", productPage.getTotalElements());

        return productPage.map(product -> {

            ProductDto dto = mapper.map(product, ProductDto.class);

            dto.setCategoryId(product.getCategory().getCategoryId());
            dto.setSupplierId(product.getSupplier().getSupplierId());

            return dto;
        });
    }

    @Override
    public ProductDto getProductById(Long id) {

        logger.info("Fetching Product Id : {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", id);

                    return new ResourceNotFoundException("Product not found");
                });

        logger.info("Product Found : {}", product.getProductName());

        ProductDto dto = mapper.map(product, ProductDto.class);

        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setSupplierId(product.getSupplier().getSupplierId());

        return dto;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {

        logger.info("Updating Product Id : {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", id);

                    return new ResourceNotFoundException("Product not found");
                });

        if (!product.getSku().equals(dto.getSku())
                && productRepository.existsBySku(dto.getSku())) {

            logger.error("SKU Already Exists : {}", dto.getSku());

            throw new DuplicateResourceException("SKU already exists");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> {

                    logger.error("Category Not Found : {}", dto.getCategoryId());

                    return new ResourceNotFoundException("Category not found");
                });

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> {

                    logger.error("Supplier Not Found : {}", dto.getSupplierId());

                    return new ResourceNotFoundException("Supplier not found");
                });

        product.setProductName(dto.getProductName());
        product.setSku(dto.getSku());
        product.setBarcode(dto.getBarcode());
        product.setDescription(dto.getDescription());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setQuantity(dto.getQuantity());
        product.setMinimumStock(dto.getMinimumStock());

        product.setCategory(category);
        product.setSupplier(supplier);

        Product updated = productRepository.save(product);

        logger.info("Product Updated Successfully : {}", updated.getProductId());

        ProductDto response = mapper.map(updated, ProductDto.class);

        response.setCategoryId(category.getCategoryId());
        response.setSupplierId(supplier.getSupplierId());

        return response;
    }

    @Override
    public void deleteProduct(Long id) {

        logger.warn("Deleting Product Id : {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", id);

                    return new ResourceNotFoundException("Product not found");
                });

        productRepository.delete(product);

        logger.info("Product Deleted Successfully : {}", id);
    }

    @Override
    public Page<ProductDto> searchProduct(
            String keyword,
            int page,
            int size) {

        logger.info("Searching Product With Keyword : {}", keyword);

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDto> products = productRepository
                .findByProductNameContainingIgnoreCase(keyword, pageable)
                .map(product -> {

                    ProductDto dto = mapper.map(product, ProductDto.class);

                    dto.setCategoryId(product.getCategory().getCategoryId());
                    dto.setSupplierId(product.getSupplier().getSupplierId());

                    return dto;
                });

        logger.info("Search Result Count : {}", products.getTotalElements());

        return products;
    }
}