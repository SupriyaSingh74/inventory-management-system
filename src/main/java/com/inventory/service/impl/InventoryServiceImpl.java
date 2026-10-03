package com.inventory.service.impl;

import com.inventory.entity.Product;
import com.inventory.entity.StockHistory;
import com.inventory.enums.StockType;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.StockHistoryRepository;
import com.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;

    private final StockHistoryRepository stockHistoryRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public void increaseStock(Long productId,
                              Integer quantity,
                              String remarks) {

        logger.info("Increasing Stock | Product Id : {} | Quantity : {}",
                productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", productId);

                    return new ResourceNotFoundException("Product not found");
                });

        int previousStock = product.getQuantity();

        int currentStock = previousStock + quantity;

        product.setQuantity(currentStock);

        productRepository.save(product);

        saveHistory(
                product,
                StockType.STOCK_IN,
                quantity,
                previousStock,
                currentStock,
                remarks
        );

        logger.info("Stock Increased Successfully | Product Id : {} | Previous : {} | Current : {}",
                productId, previousStock, currentStock);
    }

    @Override
    public void decreaseStock(Long productId,
                              Integer quantity,
                              String remarks) {

        logger.info("Decreasing Stock | Product Id : {} | Quantity : {}",
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

        int currentStock = previousStock - quantity;

        product.setQuantity(currentStock);

        productRepository.save(product);

        saveHistory(
                product,
                StockType.STOCK_OUT,
                quantity,
                previousStock,
                currentStock,
                remarks
        );

        logger.info("Stock Decreased Successfully | Product Id : {} | Previous : {} | Current : {}",
                productId, previousStock, currentStock);
    }

    @Override
    public void adjustStock(Long productId,
                            Integer newQuantity,
                            String remarks) {

        logger.info("Adjusting Stock | Product Id : {} | New Quantity : {}",
                productId, newQuantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {

                    logger.error("Product Not Found : {}", productId);

                    return new ResourceNotFoundException("Product not found");
                });

        int previousStock = product.getQuantity();

        product.setQuantity(newQuantity);

        productRepository.save(product);

        saveHistory(
                product,
                StockType.ADJUSTMENT,
                newQuantity,
                previousStock,
                newQuantity,
                remarks
        );

        logger.info("Stock Adjusted Successfully | Product Id : {} | Previous : {} | Current : {}",
                productId, previousStock, newQuantity);
    }

    private void saveHistory(Product product,
                             StockType stockType,
                             Integer quantity,
                             Integer previousStock,
                             Integer currentStock,
                             String remarks) {

        logger.info("Saving Stock History | Product Id : {} | Type : {}",
                product.getProductId(), stockType);

        StockHistory history = new StockHistory();

        history.setProduct(product);
        history.setStockType(stockType);
        history.setQuantity(quantity);
        history.setPreviousStock(previousStock);
        history.setCurrentStock(currentStock);
        history.setRemarks(remarks);

        stockHistoryRepository.save(history);

        logger.info("Stock History Saved Successfully | Product Id : {}",
                product.getProductId());
    }

}