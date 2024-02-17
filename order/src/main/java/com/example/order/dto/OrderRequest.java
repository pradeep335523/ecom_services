package com.example.order.dto;

import com.example.order.entity.OrderLineItems;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long Id;
    private String orderId;
    private List<OrderLineItemsRequest> orderLineItemsRequests;
}
