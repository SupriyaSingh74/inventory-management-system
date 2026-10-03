package com.inventory.service.impl;

import com.inventory.dto.PurchaseDto;
import com.inventory.dto.PurchaseItemDto;
import com.inventory.dto.PurchaseItemResponseDto;
import com.inventory.dto.PurchaseResponseDto;
import com.inventory.entity.Product;
import com.inventory.entity.Purchase;
import com.inventory.entity.PurchaseItem;
import com.inventory.entity.Supplier;
import com.inventory.enums.PurchaseStatus;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.PurchaseRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.service.InventoryService;
import com.inventory.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final SupplierRepository supplierRepository;

    private final ProductRepository productRepository;

    private final InventoryService inventoryService;

    private static final Logger logger =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private String generateInvoiceNumber() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        long count = purchaseRepository.count() + 1;

        return "PUR-" + date + "-"
                + String.format("%04d", count);
    }

    @Override
    public PurchaseResponseDto createPurchase(PurchaseDto dto) {

        logger.info("Creating Purchase For Supplier Id : {}", dto.getSupplierId());

        Supplier supplier = supplierRepository
                .findById(dto.getSupplierId())
                .orElseThrow(() -> {

                    logger.error("Supplier Not Found : {}", dto.getSupplierId());

                    return new ResourceNotFoundException(
                            "Supplier not found");
                });

        Purchase purchase = new Purchase();

        purchase.setInvoiceNo(generateInvoiceNumber());
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setStatus(PurchaseStatus.COMPLETED);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<PurchaseItem> purchaseItems = new ArrayList<>();

        for (PurchaseItemDto itemDto : dto.getItems()) {

            Product product = productRepository
                    .findById(itemDto.getProductId())
                    .orElseThrow(() -> {

                        logger.error("Product Not Found : {}", itemDto.getProductId());

                        return new ResourceNotFoundException(
                                "Product not found");
                    });

            logger.info("Adding Product : {} | Quantity : {}",
                    product.getProductName(),
                    itemDto.getQuantity());

            BigDecimal itemTotal =
                    itemDto.getPurchasePrice()
                            .multiply(BigDecimal.valueOf(
                                    itemDto.getQuantity()));

            PurchaseItem purchaseItem = new PurchaseItem();

            purchaseItem.setPurchase(purchase);
            purchaseItem.setProduct(product);
            purchaseItem.setQuantity(itemDto.getQuantity());
            purchaseItem.setPurchasePrice(itemDto.getPurchasePrice());
            purchaseItem.setTotal(itemTotal);

            purchaseItems.add(purchaseItem);

            totalAmount = totalAmount.add(itemTotal);

            inventoryService.increaseStock(
                    product.getProductId(),
                    itemDto.getQuantity(),
                    "Purchase : " + purchase.getInvoiceNo());
        }

        purchase.setPurchaseItems(purchaseItems);
        purchase.setTotalAmount(totalAmount);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        logger.info("Purchase Created Successfully | Invoice : {} | Total : {}",
                savedPurchase.getInvoiceNo(),
                savedPurchase.getTotalAmount());

        PurchaseResponseDto response = new PurchaseResponseDto();

        response.setPurchaseId(savedPurchase.getPurchaseId());
        response.setInvoiceNo(savedPurchase.getInvoiceNo());
        response.setSupplierName(savedPurchase.getSupplier().getSupplierName());
        response.setPurchaseDate(savedPurchase.getPurchaseDate());
        response.setTotalAmount(savedPurchase.getTotalAmount());
        response.setStatus(savedPurchase.getStatus().name());

        List<PurchaseItemResponseDto> itemResponses = new ArrayList<>();

        for (PurchaseItem item : savedPurchase.getPurchaseItems()) {

            PurchaseItemResponseDto itemResponse =
                    new PurchaseItemResponseDto();

            itemResponse.setProductId(item.getProduct().getProductId());
            itemResponse.setProductName(item.getProduct().getProductName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPurchasePrice(item.getPurchasePrice());
            itemResponse.setTotal(item.getTotal());

            itemResponses.add(itemResponse);
        }

        response.setItems(itemResponses);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponseDto> getAllPurchases(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching All Purchases | Page : {} | Size : {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Purchase> purchasePage =
                purchaseRepository.findAll(pageable);

        logger.info("Total Purchases Found : {}",
                purchasePage.getTotalElements());

        List<PurchaseResponseDto> responseList =
                purchasePage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return new PageImpl<>(
                responseList,
                pageable,
                purchasePage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponseDto getPurchaseById(Long id) {

        logger.info("Fetching Purchase Id : {}", id);

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Purchase Not Found : {}", id);

                    return new ResourceNotFoundException("Purchase not found");
                });

        logger.info("Purchase Found : {}", purchase.getInvoiceNo());

        return mapToResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponseDto getPurchaseByInvoice(String invoiceNo) {

        logger.info("Fetching Purchase Invoice : {}", invoiceNo);

        Purchase purchase = purchaseRepository.findByInvoiceNo(invoiceNo)
                .orElseThrow(() -> {

                    logger.error("Purchase Not Found : {}", invoiceNo);

                    return new ResourceNotFoundException("Purchase not found");
                });

        logger.info("Purchase Found : {}", purchase.getInvoiceNo());

        return mapToResponse(purchase);
    }

    private PurchaseResponseDto mapToResponse(Purchase purchase) {

        logger.info("Mapping Purchase Response | Invoice : {}",
                purchase.getInvoiceNo());

        PurchaseResponseDto response = new PurchaseResponseDto();

        response.setPurchaseId(purchase.getPurchaseId());
        response.setInvoiceNo(purchase.getInvoiceNo());
        response.setSupplierName(
                purchase.getSupplier().getSupplierName());
        response.setPurchaseDate(
                purchase.getPurchaseDate());
        response.setTotalAmount(
                purchase.getTotalAmount());
        response.setStatus(
                purchase.getStatus().name());

        List<PurchaseItemResponseDto> itemResponses =
                new ArrayList<>();

        for (PurchaseItem item : purchase.getPurchaseItems()) {

            PurchaseItemResponseDto dto =
                    new PurchaseItemResponseDto();

            dto.setProductId(item.getProduct().getProductId());
            dto.setProductName(item.getProduct().getProductName());
            dto.setQuantity(item.getQuantity());
            dto.setPurchasePrice(item.getPurchasePrice());
            dto.setTotal(item.getTotal());

            itemResponses.add(dto);
        }

        response.setItems(itemResponses);

        return response;
    }
}