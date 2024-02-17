package com.example.inventory;

import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeInventoryStocks(InventoryRepository inventoryRepository) {

        return args -> {

            Inventory inventory1 = new Inventory();
            inventory1.setQuantity(10);
            inventory1.setSkuCode("apple_iphone_13");

            Inventory inventory2 = new Inventory();
            inventory2.setQuantity(0);
            inventory2.setSkuCode("apple_iphone_14");

            inventoryRepository.save(inventory1);
            inventoryRepository.save(inventory2);
        };
    }

}
