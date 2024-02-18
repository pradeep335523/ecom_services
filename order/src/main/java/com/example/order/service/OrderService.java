package com.example.order.service;

import com.example.order.dto.*;
import com.example.order.entity.Order;
import com.example.order.entity.OrderLineItems;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${app.property.inventory.service-url}")
    private String inventoryServiceUrl;

    private final WebClient.Builder webClientBuilder;

    public void createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setOrderLineItemsList(orderRequest.getOrderLineItemsRequests().stream().map(this::mapToOrderLineItems).toList());

        List<String> skuCodeList = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        log.info("inventory service URL : {}",inventoryServiceUrl);
        // Before placing order check for stock availability, if all products are in stock then only place the order
        InventoryStockResponse[] result = webClientBuilder.build().get().uri(inventoryServiceUrl, uriBuilder ->
                uriBuilder.queryParam("skuCode", skuCodeList).build()
        ).retrieve().bodyToMono(InventoryStockResponse[].class).block();
        boolean validToPlace = Arrays.stream(result).allMatch(oneLine -> oneLine.isProductInStock());
        if (validToPlace) {
            orderRepository.save(order);
            log.info("Order Placed Successfully : {}", order.getOrderId());
        } else {
            throw new RuntimeException("Products are out of Stock, Please try later");
        }
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
