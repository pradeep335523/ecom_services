package com.example.inventory.service;

import com.example.inventory.dto.InventoryStockResponse;

import java.util.List;

public interface Inventoryservice {
    boolean checkInventoryStock(String skuCode);

    List<InventoryStockResponse> checkInventoryStocks(List<String> skuCodes);
}
