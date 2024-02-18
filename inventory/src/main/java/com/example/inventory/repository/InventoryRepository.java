package com.example.inventory.repository;

import com.example.inventory.dto.InventoryStockResponse;
import com.example.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Optional findBySkuCode(String skuCode);

    List<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
