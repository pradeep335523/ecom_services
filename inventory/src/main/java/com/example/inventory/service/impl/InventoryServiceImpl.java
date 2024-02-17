package com.example.inventory.service.impl;

import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.service.Inventoryservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements Inventoryservice {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public boolean checkInventoryStock(String skuCode) {
        Optional<Inventory> inventory = inventoryRepository.findBySkuCode(skuCode);
        return inventory.isPresent() && inventory.get().getQuantity() > 0 ? true : false;

    }
}
