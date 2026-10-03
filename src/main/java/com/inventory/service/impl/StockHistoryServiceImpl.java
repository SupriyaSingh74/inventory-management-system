package com.inventory.service.impl;

import com.inventory.dto.StockHistoryDto;
import com.inventory.entity.Product;
import com.inventory.entity.StockHistory;
import com.inventory.enums.StockType;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.StockHistoryRepository;
import com.inventory.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockHistoryServiceImpl implements StockHistoryService {

    private final ProductRepository productRepository;
    private final StockHistoryRepository stockHistoryRepository;
    private final ModelMapper mapper;

    private static final Logger logger =
            LoggerFactory.getLogger(StockHistoryServiceImpl.class);

    @Override
    public StockHistoryDto stockIn(Long productId,
                                   Integer quantity,
                                   String remarks) {

        logger.info("Stock In | Product Id : {} | Quantity : {}",
                productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", productId);

                    return new ResourceNotFoundException("Product not found");
                });

        int previousStock = product.getQuantity();

        product.setQuantity(previousStock + quantity);

        productRepository.save(product);

        StockHistory history = StockHistory.builder()
                .product(product)
                .stockType(StockType.STOCK_IN)
                .quantity(quantity)
                .previousStock(previousStock)
                .currentStock(product.getQuantity())
                .remarks(remarks)
                .build();

        StockHistory saved = stockHistoryRepository.save(history);

        logger.info("Stock In Completed | Product Id : {} | Previous : {} | Current : {}",
                productId, previousStock, product.getQuantity());

        StockHistoryDto dto = mapper.map(saved, StockHistoryDto.class);

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());

        return dto;
    }

    @Override
    public StockHistoryDto stockOut(Long productId,
                                    Integer quantity,
                                    String remarks) {

        logger.info("Stock Out | Product Id : {} | Quantity : {}",
                productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", productId);

                    return new ResourceNotFoundException("Product not found");
                });

        int previousStock = product.getQuantity();

        if (previousStock < quantity) {

            logger.error("Insufficient Stock | Product Id : {} | Available : {} | Requested : {}",
                    productId, previousStock, quantity);

            throw new IllegalArgumentException("Insufficient stock available");
        }

        product.setQuantity(previousStock - quantity);

        productRepository.save(product);

        StockHistory history = StockHistory.builder()
                .product(product)
                .stockType(StockType.STOCK_OUT)
                .quantity(quantity)
                .previousStock(previousStock)
                .currentStock(product.getQuantity())
                .remarks(remarks)
                .build();

        StockHistory saved = stockHistoryRepository.save(history);

        logger.info("Stock Out Completed | Product Id : {} | Previous : {} | Current : {}",
                productId, previousStock, product.getQuantity());

        StockHistoryDto dto = mapper.map(saved, StockHistoryDto.class);

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());

        return dto;
    }

    @Override
    public StockHistoryDto adjustStock(Long productId,
                                       Integer quantity,
                                       String remarks) {

        logger.info("Adjusting Stock | Product Id : {} | New Quantity : {}",
                productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", productId);

                    return new ResourceNotFoundException("Product not found");
                });

        int previousStock = product.getQuantity();

        product.setQuantity(quantity);

        productRepository.save(product);

        StockHistory history = StockHistory.builder()
                .product(product)
                .stockType(StockType.ADJUSTMENT)
                .quantity(quantity)
                .previousStock(previousStock)
                .currentStock(quantity)
                .remarks(remarks)
                .build();

        StockHistory saved = stockHistoryRepository.save(history);

        logger.info("Stock Adjusted Successfully | Product Id : {} | Previous : {} | Current : {}",
                productId, previousStock, quantity);

        StockHistoryDto dto = mapper.map(saved, StockHistoryDto.class);

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());

        return dto;
    }

    @Override
    public List<StockHistoryDto> getStockHistory(Long productId) {

        logger.info("Fetching Stock History | Product Id : {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", productId);

                    return new ResourceNotFoundException("Product not found");
                });

        List<StockHistoryDto> historyList = stockHistoryRepository
                .findByProductProductId(productId)
                .stream()
                .map(history -> {

                    StockHistoryDto dto = mapper.map(history, StockHistoryDto.class);

                    dto.setProductId(history.getProduct().getProductId());
                    dto.setProductName(history.getProduct().getProductName());

                    return dto;
                })
                .toList();

        logger.info("Total Stock History Records Found : {}",
                historyList.size());

        return historyList;
    }
}