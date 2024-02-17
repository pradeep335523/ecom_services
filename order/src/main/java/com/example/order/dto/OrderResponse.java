package com.example.order.dto;

import lombok.Data;

import java.util.List;


@Data
public class OrderResponse {
    private Long Id;
    private String orderId;
    private List<OrderLineItemsResponse> orderLineItemsResponses;
}