package com.inventory.service;

public interface InventoryService {

    void increaseStock(Long productId,
                       Integer quantity,
                       String remarks);

    void decreaseStock(Long productId,
                       Integer quantity,
                       String remarks);

    void adjustStock(Long productId,
                     Integer newQuantity,
                     String remarks);

}