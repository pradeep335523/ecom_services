package com.example.inventory.service.impl;

import com.example.inventory.dto.InventoryStockResponse;
import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.service.Inventoryservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryServiceImpl implements Inventoryservice {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public boolean checkInventoryStock(String skuCode) {
        Optional<Inventory> inventory = inventoryRepository.findBySkuCode(skuCode);
        return inventory.isPresent() && inventory.get().getQuantity() > 0 ? true : false;

    }

    @Override
    public List<InventoryStockResponse> checkInventoryStocks(List<String> skuCodes) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        List<InventoryStockResponse> responses = inventories.stream().map(inventory -> {
            {
                InventoryStockResponse response = InventoryStockResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isProductInStock(inventory.getQuantity() > 0).build();
                log.info("Stock List: {}", response);
                return response;
            }
        }).toList();
        log.info("Final values to return : {}", responses);
        return responses;

    }
}
