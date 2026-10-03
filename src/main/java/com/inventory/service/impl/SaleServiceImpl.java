package com.inventory.service.impl;

import com.inventory.dto.*;
import com.inventory.entity.Customer;
import com.inventory.entity.Product;
import com.inventory.entity.Sale;
import com.inventory.entity.SaleItem;
import com.inventory.enums.SaleStatus;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.CustomerRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SaleRepository;
import com.inventory.service.InventoryService;
import com.inventory.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
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
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final InventoryService inventoryService;

    private static final Logger logger =
            LoggerFactory.getLogger(SaleServiceImpl.class);

    private String generateInvoiceNumber() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        long count = saleRepository.count() + 1;

        return "SAL-" + date + "-"
                + String.format("%04d", count);
    }

    @Override
    public SaleResponseDto createSale(SaleDto dto) {

        logger.info("Creating Sale For Customer Id : {}", dto.getCustomerId());

        Customer customer = customerRepository
                .findById(dto.getCustomerId())
                .orElseThrow(() -> {

                    logger.error("Customer Not Found : {}", dto.getCustomerId());

                    return new ResourceNotFoundException(
                            "Customer not found");
                });

        Sale sale = new Sale();

        sale.setInvoiceNo(generateInvoiceNumber());
        sale.setCustomer(customer);
        sale.setSaleDate(LocalDate.now());
        sale.setStatus(SaleStatus.COMPLETED);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<SaleItem> saleItems = new ArrayList<>();

        for (SaleItemDto itemDto : dto.getItems()) {

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
                    itemDto.getSellingPrice()
                            .multiply(BigDecimal.valueOf(
                                    itemDto.getQuantity()));

            SaleItem saleItem = new SaleItem();

            saleItem.setSale(sale);
            saleItem.setProduct(product);
            saleItem.setQuantity(itemDto.getQuantity());
            saleItem.setSellingPrice(itemDto.getSellingPrice());
            saleItem.setTotal(itemTotal);

            saleItems.add(saleItem);

            totalAmount = totalAmount.add(itemTotal);

            inventoryService.decreaseStock(
                    product.getProductId(),
                    itemDto.getQuantity(),
                    "Sale : " + sale.getInvoiceNo());
        }

        sale.setSaleItems(saleItems);
        sale.setTotalAmount(totalAmount);

        Sale savedSale = saleRepository.save(sale);

        logger.info("Sale Created Successfully | Invoice : {} | Total : {}",
                savedSale.getInvoiceNo(),
                savedSale.getTotalAmount());

        SaleResponseDto response = new SaleResponseDto();

        response.setSaleId(savedSale.getSaleId());
        response.setInvoiceNo(savedSale.getInvoiceNo());
        response.setCustomerName(savedSale.getCustomer().getCustomerName());
        response.setSaleDate(savedSale.getSaleDate());
        response.setTotalAmount(savedSale.getTotalAmount());
        response.setStatus(savedSale.getStatus().name());

        List<SaleItemResponseDto> itemResponses = new ArrayList<>();

        for (SaleItem item : savedSale.getSaleItems()) {

            SaleItemResponseDto itemResponse =
                    new SaleItemResponseDto();

            itemResponse.setProductId(item.getProduct().getProductId());
            itemResponse.setProductName(item.getProduct().getProductName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setSellingPrice(item.getSellingPrice());
            itemResponse.setTotal(item.getTotal());

            itemResponses.add(itemResponse);
        }

        response.setItems(itemResponses);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaleResponseDto> getAllSales(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching All Sales | Page : {} | Size : {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Sale> salePage = saleRepository.findAll(pageable);

        logger.info("Total Sales Found : {}", salePage.getTotalElements());

        List<SaleResponseDto> responseList = salePage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new PageImpl<>(
                responseList,
                pageable,
                salePage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponseDto getSaleById(Long id) {

        logger.info("Fetching Sale Id : {}", id);

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Sale Not Found : {}", id);

                    return new ResourceNotFoundException("Sale not found");
                });

        logger.info("Sale Found : {}", sale.getInvoiceNo());

        return mapToResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponseDto getSaleByInvoice(String invoiceNo) {

        logger.info("Fetching Sale Invoice : {}", invoiceNo);

        Sale sale = saleRepository.findByInvoiceNo(invoiceNo)
                .orElseThrow(() -> {

                    logger.error("Sale Not Found : {}", invoiceNo);

                    return new ResourceNotFoundException("Sale not found");
                });

        logger.info("Sale Found : {}", sale.getInvoiceNo());

        return mapToResponse(sale);
    }

    private SaleResponseDto mapToResponse(Sale sale) {

        logger.info("Mapping Sale Response | Invoice : {}",
                sale.getInvoiceNo());

        SaleResponseDto response = new SaleResponseDto();

        response.setSaleId(sale.getSaleId());
        response.setInvoiceNo(sale.getInvoiceNo());
        response.setCustomerName(
                sale.getCustomer().getCustomerName());
        response.setSaleDate(sale.getSaleDate());
        response.setTotalAmount(sale.getTotalAmount());
        response.setStatus(sale.getStatus().name());

        List<SaleItemResponseDto> itemResponses = new ArrayList<>();

        for (SaleItem item : sale.getSaleItems()) {

            SaleItemResponseDto dto = new SaleItemResponseDto();

            dto.setProductId(item.getProduct().getProductId());
            dto.setProductName(item.getProduct().getProductName());
            dto.setQuantity(item.getQuantity());
            dto.setSellingPrice(item.getSellingPrice());
            dto.setTotal(item.getTotal());

            itemResponses.add(dto);
        }

        response.setItems(itemResponses);

        return response;
    }
}