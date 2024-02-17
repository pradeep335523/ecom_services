package com.example.order.service;

import com.example.order.dto.OrderLineItemsRequest;
import com.example.order.dto.OrderLineItemsResponse;
import com.example.order.dto.OrderRequest;
import com.example.order.dto.OrderResponse;
import com.example.order.entity.Order;
import com.example.order.entity.OrderLineItems;
import com.example.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId =UUID.randomUUID().toString();
        log.info("Generated Order ID : {}",orderId);
        order.setOrderId(orderId);
        order.setOrderLineItemsList(orderRequest.getOrderLineItemsRequests().stream().map(this::mapToOrderLineItems).toList());
        orderRepository.save(order);
        log.
                info("Order Placed Successfully : {}", order.getOrderId());
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsRequest orderLineItemsRequest) {
        OrderLineItems lineItems = new OrderLineItems();
        lineItems.setQuantity(orderLineItemsRequest.getQuantity());
        lineItems.setPrice(orderLineItemsRequest.getPrice());
        lineItems.setSkuCode(orderLineItemsRequest.getSkuCode());
        return lineItems;
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToOrders).toList();

    }

    private OrderResponse mapToOrders(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderId(order.getOrderId());
        response.setOrderLineItemsResponses(order.getOrderLineItemsList().stream().map(this::mapToOrderLinesResponse).toList());
        return response;
    }

    private OrderLineItemsResponse mapToOrderLinesResponse(OrderLineItems orderLineItems) {
        OrderLineItemsResponse orderLineItemsResponse = new OrderLineItemsResponse();
        orderLineItemsResponse.setId(orderLineItems.getId());
        orderLineItemsResponse.setQuantity(orderLineItems.getQuantity());
        orderLineItemsResponse.setPrice(orderLineItems.getPrice());
        orderLineItemsResponse.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItemsResponse;
    }
}
