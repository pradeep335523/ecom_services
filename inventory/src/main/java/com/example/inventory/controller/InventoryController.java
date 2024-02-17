package com.example.inventory.controller;

import com.example.inventory.service.Inventoryservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {
@Autowired
private Inventoryservice inventoryservice;
    @GetMapping("/inventory/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInventoryAvailable(@PathVariable("skuCode") String skuCode)
    {
        return inventoryservice.checkInventoryStock(skuCode);
    }
}
